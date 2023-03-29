package net.spellbladenext.entities;

import io.netty.buffer.Unpooled;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.FlyingItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.spell_engine.api.spell.Spell;
import net.spell_engine.entity.SpellProjectile;
import net.spell_engine.internals.SpellHelper;
import net.spell_engine.utils.TargetHelper;
import net.spell_power.api.SpellPower;
import net.spellbladenext.SpellbladeNext;
import net.spellbladenext.items.FriendshipBracelet;

import java.util.List;
import java.util.Random;
import java.util.function.Predicate;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

public class IcicleBarrierSpellProjectile extends SpellProjectile implements FlyingItemEntity {
    public LivingEntity target;
    public double damage = 1;
    public SpellPower.Result power;
    public Spell spell;
    public SpellHelper.ImpactContext context;

    public IcicleBarrierSpellProjectile(EntityType<? extends IcicleBarrierSpellProjectile> entityType, World world, PlayerEntity playerEntity) {
        super(entityType, world);
        this.setOwner(playerEntity);
        Vec3d rotationVec = playerEntity.getRotationVec(0);
        this.setNoGravity(true);
        double d0 = rotationVec.horizontalLength();
        this.setYaw(((float) (MathHelper.atan2(playerEntity.getRotationVec(0).getX(), playerEntity.getRotationVec(0).getZ()) * (double) (180F / (float) Math.PI)))-90);
        this.setPitch((float) (MathHelper.atan2(playerEntity.getRotationVec(0).getY(), d0) * (double) (180F / (float) Math.PI))-45);
        this.prevYaw = ((float) (MathHelper.atan2(playerEntity.getRotationVec(0).getX(), playerEntity.getRotationVec(0).getZ()) * (double) (180F / (float) Math.PI)));
        this.prevPitch = (float) (MathHelper.atan2(playerEntity.getRotationVec(0).getY(), d0) * (double) (180F / (float) Math.PI));
    }
    @Override
    public Behaviour behaviour() {
        return Behaviour.FLY;
    }

    public IcicleBarrierSpellProjectile(EntityType<? extends IcicleBarrierSpellProjectile> entityType, World world) {
        super(entityType, world);
        this.setNoGravity(true);
        
        if(this.getOwner() != null && this.getOwner() instanceof PlayerEntity playerEntity) {

            Vec3d playerEntityRotationVec = playerEntity.getRotationVec(0);
            this.setNoGravity(true);

            this.setYaw(((float) (MathHelper.atan2(playerEntity.getRotationVec(0).getX(), playerEntity.getRotationVec(0).getZ()) * (double) (180F / (float) Math.PI))) - 90);
            this.setPitch((float) (MathHelper.atan2(playerEntity.getRotationVec(0).getY(), playerEntityRotationVec.horizontalLength()) * (double) (180F / (float) Math.PI)) - 45);
            this.prevYaw = ((float) (MathHelper.atan2(playerEntity.getRotationVec(0).getX(), playerEntity.getRotationVec(0).getZ()) * (double) (180F / (float) Math.PI)));
            this.prevPitch = (float) (MathHelper.atan2(playerEntity.getRotationVec(0).getY(), playerEntityRotationVec.horizontalLength()) * (double) (180F / (float) Math.PI));
        }
    }

    @Override
    public void tick() {

        this.lastRenderX = this.getX();
        this.lastRenderY = this.getY();
        this.lastRenderZ = this.getZ();
        //ParticleHelper.play(this.getWorld(),this,this.getXRot(),this.getYRot(), new ParticleBatch("spell_engine:snowflake", ParticleBatch.Shape.CIRCLE, ParticleBatch.Origin.CENTER, ParticleBatch.Rotation.LOOK,3,0,0,0));

        if(this.age > 50 && !this.getWorld().isClient()){
            this.discard();
        }
        if(this.getOwner() == null){
            this.discard();
            return;
        }
        double number2 = 0;
        float f7 = this.getOwner().getYaw() % 360;
        float f = this.getOwner().getPitch();
        float f1 = -MathHelper.sin(f7 * ((float) Math.PI / 180F)) * MathHelper.cos(f * ((float) Math.PI / 180F));
        float f2 = -MathHelper.sin(f * ((float) Math.PI / 180F));
        float f3 = MathHelper.cos(f7 * ((float) Math.PI / 180F)) * MathHelper.cos(f * ((float) Math.PI / 180F));
        int i = 1000 - this.age * 20;

        double[] indices = IntStream.rangeClosed(0, (1000))
                .mapToDouble(x -> x).toArray();
        if(i < 0) {
            return;
        }
        double phi = Math.acos(1 - 2 * indices[i] / 1000);
        double theta = Math.PI * (1 + Math.pow(5, 0.5) * indices[i]);
        if(phi == Math.toRadians(180)  && theta == Math.toRadians(180)){
            this.setInvisible(true);
        }
        double x = cos(theta) * sin(phi);
        double y = -cos(phi);
        double z = Math.sin(theta) * sin(phi);
        Vec3d rotationVec = rotate(x, y, z, -Math.toRadians(f7), Math.toRadians(f + 90),0);

        this.setPos(
                this.getOwner().getCameraPosVec(1.0f).getX() + 4 * rotationVec.x + number2 * f1,
                this.getOwner().getCameraPosVec(1.0f).getY() + 4 * rotationVec.y + number2 * f2,
                this.getOwner().getCameraPosVec(1.0f).getZ() + 4 * rotationVec.getZ() + number2 * f3);
        Vec3d vec3d = new Vec3d(4 * rotationVec.x + number2 * f1,4 * rotationVec.y + number2 * f2,4 * rotationVec.z + number2 * f3);
        this.setYaw((float)(MathHelper.atan2(vec3d.getX(), vec3d.getZ()) * (double)(180F / (float)Math.PI)) - 90);
        this.setPitch((float)(MathHelper.atan2(vec3d.getY(), vec3d.horizontalLength()) * (double)(180F / (float)Math.PI)) + 45);
        this.prevYaw = this.getYaw();
        this.prevPitch = this.getPitch();

        // TODO: I have no idea what you're trying to do here. Are you just trying to spawn particles on the captured entities?
        if (this.getOwner() instanceof PlayerEntity playerEntity) {
            Predicate<Entity> selectionPredicate = (target) -> (TargetHelper.actionAllowed(TargetHelper.TargetingMode.AREA, TargetHelper.Intent.HARMFUL, playerEntity, target)
                    && FriendshipBracelet.PlayerFriendshipPredicate(playerEntity,target));
            Spell.Release.Target.Area area = new Spell.Release.Target.Area();
            area.angle_degrees = 360;
            List<Entity> entities = this.getWorld().getEntitiesByClass(Entity.class,this.getBoundingBox().expand(1),selectionPredicate);
            entities.removeIf(asdf -> asdf == this);
            if (!entities.isEmpty()) {
                if (this.power != null && this.spell != null && this.context != null) {
                    for (Entity target : entities) {
                        if (target != null && this.getOwner() instanceof LivingEntity living && target != this.getOwner()) {
                            if (target.timeUntilRegen <= 10) {
                                SpellHelper.performImpacts(this.getWorld(), living, target, this.spell, this.context);
                                target.timeUntilRegen = 20;

                            }
                            if (world.getServer() != null) {
                                int[] intarray;
                                intarray = new int[3];
                                intarray[0] = (int) Math.round(target.getBoundingBox().getCenter().x);
                                intarray[1] = (int) Math.round(target.getBoundingBox().getCenter().y);
                                intarray[2] = (int) Math.round(target.getBoundingBox().getCenter().z);
                                Stream<ServerPlayerEntity> serverplayers = world.getServer().getPlayerManager().getPlayerList().stream();

                                for (ServerPlayerEntity serverPlayerEntity : ((ServerWorld) world).getPlayers(serverPlayerEntity -> serverPlayerEntity.canSee(this.getOwner()))) {
                                    PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer()).writeIntArray(intarray);
                                    Random rand = new Random();
                                    Vec3d vec3 = target.getBoundingBox().getCenter().add(new Vec3d(rand.nextDouble(-1, 1), rand.nextDouble(-1, 1), rand.nextDouble(-1, 1)));
                                    ((ServerWorld) world).spawnParticles(ParticleTypes.SWEEP_ATTACK, vec3.getX(), vec3.getY(), vec3.getZ(), 1, 0, 0, 0, 0);
                                }
                            }

                            Random rand = new Random();
                            Vec3d vec3 = target.getBoundingBox().getCenter().add(new Vec3d(rand.nextDouble(-2, 2), rand.nextDouble(-2, 2), rand.nextDouble(-2, 2)));
                            Vec3d vec31 = target.getBoundingBox().getCenter().subtract(vec3).normalize();
                        }
                    }
                }
            }
        }
    }


    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
    }

    @Override
    protected void onBlockHit(BlockHitResult blockHitResult) {

    }
    @Override
    public boolean isAttackable() {
        return false;
    }
    @Override
    public ItemStack getStack() {
        return SpellbladeNext.FROSTBLADE.get().getDefaultStack();
    }

    public Vec3d rotate(double x, double y, double z, double pitch, double roll, double yaw) {
        double cosa = Math.cos(yaw);
        double sina = Math.sin(yaw);

        double cosb = Math.cos(pitch);
        double sinb = Math.sin(pitch);
        double cosc = Math.cos(roll);
        double sinc = Math.sin(roll);

        double Axx = cosa * cosb;
        double Axy = cosa * sinb * sinc - sina * cosc;
        double Axz = cosa * sinb * cosc + sina * sinc;

        double Ayx = sina * cosb;
        double Ayy = sina * sinb * sinc + cosa * cosc;
        double Ayz = sina * sinb * cosc - cosa * sinc;

        double Azx = -sinb;
        double Azy = cosb * sinc;
        double Azz = cosb * cosc;

        return new Vec3d(Axx * x + Axy * y + Axz * z,Ayx * x + Ayy * y + Ayz * z,Azx * x + Azy * y + Azz * z);
    }

}
