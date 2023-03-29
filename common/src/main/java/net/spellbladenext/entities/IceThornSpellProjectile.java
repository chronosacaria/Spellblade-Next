package net.spellbladenext.entities;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.FlyingItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.spell_engine.api.spell.Spell;
import net.spell_engine.entity.SpellProjectile;
import net.spell_engine.internals.SpellHelper;
import net.spell_engine.utils.TargetHelper;
import net.spell_power.api.SpellPower;
import net.spellbladenext.items.FriendshipBracelet;

import java.util.List;
import java.util.function.Predicate;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

public class IceThornSpellProjectile extends SpellProjectile implements FlyingItemEntity {
    public SpellPower.Result power;
    public int number;
    private Entity target;
    public Spell spell;
    public SpellHelper.ImpactContext context;

    public IceThornSpellProjectile(EntityType<? extends IceThornSpellProjectile> entityType, World world, PlayerEntity playerEntity) {
        super(entityType, world);
        this.setOwner(playerEntity);
        Vec3d playerEntityRotationVec = playerEntity.getRotationVec(0);
        this.setNoGravity(true);

        double d0 = playerEntityRotationVec.horizontalLength();
        this.setYaw(((float) (MathHelper.atan2(playerEntity.getRotationVec(0).getX(), playerEntity.getRotationVec(0).getZ()) * (double) (180F / (float) Math.PI))));
        this.setPitch((float) (MathHelper.atan2(playerEntity.getRotationVec(0).getY(), d0) * (double) (180F / (float) Math.PI)));
        this.prevYaw = ((float) (MathHelper.atan2(playerEntity.getRotationVec(0).getX(), playerEntity.getRotationVec(0).getZ()) * (double) (180F / (float) Math.PI)));
        this.prevPitch = (float) (MathHelper.atan2(playerEntity.getRotationVec(0).getY(), d0) * (double) (180F / (float) Math.PI));

    }
    @Override
    public boolean isAttackable() {
        return false;
    }
    public IceThornSpellProjectile(EntityType<? extends IceThornSpellProjectile> entityType, World world) {
        super(entityType, world);
        this.setNoGravity(true);

    }

    public static Vec3d rotate(double x, double y, double z, double pitch, double roll, double yaw) {
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

    @Override
    public Behaviour behaviour() {
        return Behaviour.FLY;
    }
    @Override
    public void tick() {
        if(this.age < 60 && this.target != null && this.getOwner() instanceof PlayerEntity playerEntity && this.spell != null && this.context != null && this.target.timeUntilRegen <= 10){
            Vec3d vec3 = this.target.getCameraPosVec(1.0F).subtract(this.getPos());
            this.setVelocity(this.getVelocity().multiply(0.95D).add(vec3.normalize().multiply(0.05)));
        }
        if(this.age > 40){
            super.tick();
            Vec3d velocityVec = this.getVelocity();
            this.setYaw((float) (MathHelper.atan2(velocityVec.getX(), velocityVec.getZ()) * (double) (180F / (float) Math.PI)) - 90);
            this.setPitch((float) (MathHelper.atan2(velocityVec.getY(), velocityVec.horizontalLength()) * (double) (180F / (float) Math.PI)) + 45);
            this.prevYaw = this.getYaw();
            this.prevPitch = this.getPitch();
        }
        if(this.age < 40 && this.getOwner() != null &&  this.getOwner().isAlive()) {
            double tridentEntity = this.getOwner().getHeadYaw() ;
            double x = (2D +  (getOwner().getBoundingBox().getXLength())) * 0.5  * cos(((((double) (this.age % 40) / 40D)) * (2D * Math.PI) + (2 * Math.PI * (double) (number % 4) / 4D)));
            double z = (2D + getOwner().getBoundingBox().getXLength()) * 0.5 * sin(((((double) (this.age % 40) / 40D)) * (2 * Math.PI) + (2 * Math.PI * (double) (number % 4) / 4D)));
            Vec3d rotationVec = rotate(x,0D,z, -Math.toRadians(tridentEntity+90),0D,0D);

            this.lastRenderX = this.getX();
            this.lastRenderY = this.getY();
            this.lastRenderZ = this.getZ();

            this.setPos(this.getOwner().getX() + rotationVec.getX() , this.getOwner().getBoundingBox().getCenter().getY(), this.getOwner().getZ()  + rotationVec.getZ() );

            Vec3d subtractionVec = this.getPos().subtract(this.getOwner().getPos().add(0,this.getOwner().getBoundingBox().getYLength() / 2,0));

            this.setYaw((float) (MathHelper.atan2(subtractionVec.getX(), subtractionVec.getZ()) * (double) (180F / (float) Math.PI)) - 90);
            this.setPitch((float) (MathHelper.atan2(subtractionVec.getY(), subtractionVec.horizontalLength()) * (double) (180F / (float) Math.PI)) + 45);
            this.prevYaw = this.getYaw();
            this.prevPitch = this.getPitch();

        }
        if(this.age == 40 && this.getOwner() instanceof PlayerEntity playerEntity) {
            Predicate<Entity> selectionPredicate = (target) -> (TargetHelper.actionAllowed(TargetHelper.TargetingMode.AREA, TargetHelper.Intent.HARMFUL, playerEntity, target)
                    && FriendshipBracelet.PlayerFriendshipPredicate(playerEntity,target));
            Spell.Release.Target.Area area = new Spell.Release.Target.Area();
            area.angle_degrees = 100;
            List<Entity> list = TargetHelper.targetsFromArea(playerEntity, playerEntity.getCameraPosVec(1.0F),16F, area, selectionPredicate);
            if(!list.isEmpty()){
                this.target = list.get(this.random.nextInt(list.toArray().length));
            }
            if(!list.isEmpty()) {
                if(Registry.SOUND_EVENT.get(new Identifier("spell_engine", "generic_frost_release")) != null) {
                    this.getWorld().playSound(null, this.getX(), this.getY(), this.getZ(), Registry.SOUND_EVENT.get(new Identifier("spell_engine", "generic_frost_release")), this.getOwner().getSoundCategory(), 1F, 1.0F);
                }
            }
            if(this.target != null){
                this.setVelocity(0,1,0);
            }
        }

        if(this.age > 60 && this.target != null){
            if(this.getBoundingBox().stretch(this.getVelocity()).intersects(this.target.getBoundingBox()) && this.power != null){
                if(this.getWorld() instanceof ServerWorld && this.getOwner() instanceof PlayerEntity playerEntity){
                    for (int i = 0; i < 50; ++i) {
                        final double angle = Math.toRadians(((double) i / 50) * 360d);

                        double x = cos(angle) * 2;
                        double y = sin(angle) * 2;

                        if(!this.getWorld().isClient())
                            ((ServerWorld) this.getWorld()).spawnParticles(ParticleTypes.SNOWFLAKE, target.getX(), target.getBodyY(0.5D), target.getZ(), 1, x, 0.0D, y, 0.2D);

                    }
                    Predicate<Entity> selectionPredicate = (target) -> (TargetHelper.actionAllowed(TargetHelper.TargetingMode.DIRECT, TargetHelper.Intent.HARMFUL, playerEntity, target)
                            && FriendshipBracelet.PlayerFriendshipPredicate(playerEntity,target));
                    Spell.Release.Target.Area area = new Spell.Release.Target.Area();
                    area.angle_degrees = 360;
                    List<Entity> list = TargetHelper.targetsFromArea(this, this.target.getCameraPosVec(1.0F),2F, area,  selectionPredicate);
                    list.add(this.target);
                    for (Entity entity: list
                    ) {
                        if(entity.timeUntilRegen <= 10) {
                            SpellHelper.performImpacts(this.getWorld(), playerEntity, entity, this.spell, this.context);
                            entity.timeUntilRegen = 20;

                        }
                    }
                    this.discard();

                }
                this.discard();
            }
            Vec3d vec3 = this.target.getCameraPosVec(1.0F).subtract(this.getPos());
            this.setVelocity(this.getVelocity().multiply(0.95D).add(vec3.normalize().multiply(0.5)));

        }

        if(this.age > 40 && (this.target == null) &&  !this.getWorld().isClient()){
            this.discard();

        }
        if(this.age > 200 && !this.getWorld().isClient())
        {
            this.discard();
        }
        if(this.getVelocity().length() > 1){
            this.setVelocity(this.getVelocity().normalize());
        }
    }



    @Override
    protected void onBlockHit(BlockHitResult blockHitResult) {
        if(this.getWorld() instanceof ServerWorld && this.getOwner() instanceof PlayerEntity playerEntity){
            for (int i = 0; i < 50; ++i) {
                final double angle = Math.toRadians(((double) i / 50) * 360d);

                double x = Math.cos(angle) * 2;
                double y = Math.sin(angle) * 2;

                if(!this.getWorld().isClient())
                    ((ServerWorld) this.getWorld()).spawnParticles(ParticleTypes.SNOWFLAKE, blockHitResult.getBlockPos().getX(), blockHitResult.getBlockPos().getY(), blockHitResult.getBlockPos().getZ(), 1, x, 0.0D, y, 0.2D);

            }
            Predicate<Entity> selectionPredicate = (target) -> (TargetHelper.actionAllowed(TargetHelper.TargetingMode.DIRECT, TargetHelper.Intent.HARMFUL, playerEntity, target)
                    && FriendshipBracelet.PlayerFriendshipPredicate(playerEntity,target));
            Spell.Release.Target.Area area = new Spell.Release.Target.Area();
            area.angle_degrees = 360;
            List<Entity> list = TargetHelper.targetsFromArea(this, Vec3d.ofCenter(blockHitResult.getBlockPos().up()),2F, area,  selectionPredicate);

            for (Entity entity: list
                 ) {
                if(entity.timeUntilRegen <= 10 && SpellHelper.performImpacts(this.getWorld(), playerEntity, entity, this.spell, this.context)){

                    entity.timeUntilRegen = 20;

                }
            }
            if(Registry.SOUND_EVENT.get(new Identifier("spell_engine", "generic_frost_impact")) != null) {
                this.getWorld().playSound(null, blockHitResult.getBlockPos().getX(), blockHitResult.getBlockPos().up().getY(), blockHitResult.getBlockPos().getZ(), Registry.SOUND_EVENT.get(new Identifier("spell_engine", "generic_frost_impact")), this.getOwner().getSoundCategory(), 1F, 1.0F);
            }
            this.discard();

        }

    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        if(this.getOwner() instanceof PlayerEntity playerEntity){
            for (int i = 0; i < 50; ++i) {
                final double angle = Math.toRadians(((double) i / 50) * 360d);

                double x = Math.cos(angle) * 2;
                double y = Math.sin(angle) * 2;

                if(this.getWorld() instanceof ServerWorld serverWorld)
                    serverWorld.spawnParticles(ParticleTypes.SNOWFLAKE, target.getX(), target.getBodyY(0.5D), target.getZ(), 1, x, 0.0D, y, 0.2D);

            }
            Predicate<Entity> selectionPredicate = (target) -> (TargetHelper.actionAllowed(TargetHelper.TargetingMode.DIRECT, TargetHelper.Intent.HARMFUL, playerEntity, target)
                    && FriendshipBracelet.PlayerFriendshipPredicate(playerEntity,target));
            Spell.Release.Target.Area area = new Spell.Release.Target.Area();
            area.angle_degrees = 360;
            List<Entity> list = TargetHelper.targetsFromArea(this, entityHitResult.getEntity().getCameraPosVec(1.0F),2F, area,  selectionPredicate);
            list.add(entityHitResult.getEntity());

            for (Entity entity: list
            ) {
                if(entity.timeUntilRegen <= 10 && SpellHelper.performImpacts(this.getWorld(), playerEntity, entity, this.spell, this.context)) {

                    entity.timeUntilRegen = 20;

                }
            }
            if(!this.getWorld().isClient()) {
                this.discard();
            }

        }
    }

    @Override
    public ItemStack getStack() {
        return Items.SNOWBALL.getDefaultStack();
    }
}
