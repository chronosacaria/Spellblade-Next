package net.spellbladenext.entities;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.FlyingItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.spell_engine.SpellEngineMod;
import net.spell_engine.api.spell.Spell;
import net.spell_engine.client.render.FlyingSpellEntity;
import net.spell_engine.entity.SpellProjectile;
import net.spell_engine.internals.SpellHelper;
import net.spell_engine.utils.RecordsWithGson;
import net.spellbladenext.SpellbladeNext;

import java.util.ArrayList;
import java.util.List;

public class EruptionSpellProjectile extends SpellProjectile implements FlyingSpellEntity, FlyingItemEntity {
    public float range;
    private Spell spell;
    private SpellHelper.ImpactContext context;
    private Entity followedTarget;
    public Vec3d previousVelocity;
    public int vertical = 0;
    public int horizontal = 0;
    private Spell.ProjectileData clientSyncedData;
    private static final String NBT_SPELL_DATA = "Spell.Data";
    private static final String NBT_IMPACT_CONTEXT = "Impact.Context";
    private static final TrackedData<String> BEHAVIOUR;
    private static final TrackedData<String> CLIENT_DATA;
    private static final TrackedData<Integer> TARGET_ID;
    private boolean inGround = false;

    public EruptionSpellProjectile(EntityType<? extends EruptionSpellProjectile> entityType, World world) {
        super(entityType, world);
    }

    protected EruptionSpellProjectile(World world, LivingEntity owner) {
        super(SpellEngineMod.SPELL_PROJECTILE, world);
        this.range = 128.0F;
        this.setOwner(owner);
    }

    public EruptionSpellProjectile(World world, LivingEntity caster, double x, double y, double z, SpellProjectile.Behaviour behaviour, Spell spell, Entity target, SpellHelper.ImpactContext context) {
        this(world, caster);
        this.setPos(x, y, z);
        this.spell = spell;
        Spell.ProjectileData projectileData = this.projectileData();
        Gson gson = new Gson();
        this.context = context;
        this.getDataTracker().set(CLIENT_DATA, gson.toJson(projectileData));
        this.getDataTracker().set(BEHAVIOUR, behaviour.toString());
        this.setFollowedTarget(target);
    }

    List<Vec3d> vec3dArrayList = new ArrayList<>();
    public ItemStack getItem(){
        return Items.AIR.getDefaultStack();
    }
    @Override
    public void tick() {
        this.baseTick();
        this.setNoGravity(true);
        if(this.age % 3 == 1 && this.getImpactContext() != null && this.getSpell() != null && this.getOwner() != null) {
            this.vec3dArrayList.add(this.getPos());
            ProjectileUtil.setRotationFromVelocity(this, 1F);
            for (Vec3d vec3 : this.vec3dArrayList) {
                if (!this.getWorld().isClient()) {
                    for (int i = 0; i < 3; i++) {
                        CleansingFlameEntity cleansingFlameEntity = new CleansingFlameEntity(SpellbladeNext.CLEANSING_FLAME_ENTITY_ENTITY_TYPE, this.getWorld());
                        cleansingFlameEntity.target = this.getFollowedTarget();
                        cleansingFlameEntity.context = this.getImpactContext();
                        cleansingFlameEntity.power = this.getImpactContext().power();
                        cleansingFlameEntity.spell = this.getSpell();
                        cleansingFlameEntity.setOwner(this.getOwner());
                        cleansingFlameEntity.setPosition(
                                vec3.add(
                                        -4 + 8 * this.random.nextDouble(),
                                        -4 + 8 * this.random.nextDouble(),
                                        -4 + 8 * this.random.nextDouble()
                                )
                        );
                        int negOrPos = this.random.nextBoolean() ? 1 : -1;
                        cleansingFlameEntity.life = 20;
                        cleansingFlameEntity.setPitch(this.getPitch());
                        cleansingFlameEntity.setYaw(this.getYaw());
                        if(vertical == 1)
                            cleansingFlameEntity.setVelocity(
                                    this,
                                    -cleansingFlameEntity.getPitch() + 90 * vertical * negOrPos,
                                    cleansingFlameEntity.getYaw() + 90 * horizontal * negOrPos,
                                    0,
                                    1,
                                    0
                            );
                        if(horizontal == 1)
                            cleansingFlameEntity.setVelocity(
                                    this,
                                    0,
                                    cleansingFlameEntity.getYaw() + 90 * horizontal * negOrPos,
                                    0,
                                    1,
                                    0
                            );

                        cleansingFlameEntity.setVelocity(cleansingFlameEntity.getVelocity().subtract(this.getVelocity()));
                        this.getWorld().spawnEntity(cleansingFlameEntity);
                    }
                }
            }
        }
        if(this.age < 40 && !this.inGround) {
            this.setPosition(this.getPos().add(this.getVelocity()));
        }
        if(this.age > 40 && !this.vec3dArrayList.isEmpty()){
            this.vec3dArrayList.remove(0);
        }
        if(this.age > 40 && !this.getWorld().isClient() && this.vec3dArrayList.isEmpty())
            this.discard();
        if(this.age > 60 && !this.getWorld().isClient() )
            this.discard();
    }

    @Override
    protected float getDrag() {
        return 1F;
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
    }

    @Override
    public Spell getSpell() {
        return this.spell;
    }

    @Override
    public SpellHelper.ImpactContext getImpactContext() {
        return this.context;
    }

    @Override
    public Spell.ProjectileData.Client renderData() {
        Spell.ProjectileData data = this.projectileData();
        return data != null ? this.projectileData().client_data : null;
    }

    @Override
    protected void onBlockHit(BlockHitResult blockHitResult) {
        this.inGround = true;
        super.onBlockHit(blockHitResult);

    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        Gson gson = new Gson();
        nbt.putString(NBT_SPELL_DATA, gson.toJson(this.spell));
        nbt.putString(NBT_IMPACT_CONTEXT, gson.toJson(this.context));
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        if (nbt.contains(NBT_SPELL_DATA, 8)) {
            try {
                Gson gson = new Gson();
                this.spell = gson.fromJson(nbt.getString(NBT_SPELL_DATA), Spell.class);
                Gson recordReader = (new GsonBuilder()).registerTypeAdapterFactory(new RecordsWithGson.RecordTypeAdapterFactory()).create();
                this.context = recordReader.fromJson(nbt.getString(NBT_IMPACT_CONTEXT), SpellHelper.ImpactContext.class);
            } catch (Exception var4) {
                System.err.println("SpellProjectile - Failed to read spell data from NBT");
            }
        }
    }

    @Override
    protected void initDataTracker() {
        new Gson();
        this.getDataTracker().startTracking(CLIENT_DATA, "");
        this.getDataTracker().startTracking(TARGET_ID, 0);
        this.getDataTracker().startTracking(BEHAVIOUR, SpellProjectile.Behaviour.FLY.toString());
    }

    static {
        CLIENT_DATA = DataTracker.registerData(EruptionSpellProjectile.class, TrackedDataHandlerRegistry.STRING);
        TARGET_ID = DataTracker.registerData(EruptionSpellProjectile.class, TrackedDataHandlerRegistry.INTEGER);
        BEHAVIOUR = DataTracker.registerData(EruptionSpellProjectile.class, TrackedDataHandlerRegistry.STRING);
    }

    public enum Behaviour {
        FLY,
        FALL;

        Behaviour() {
        }
    }
    private Spell.ProjectileData projectileData() {
        return this.getWorld().isClient ? this.clientSyncedData : this.spell.release.target.projectile;
    }

    private void setFollowedTarget(Entity target) {
        this.followedTarget = target;
        int id = 0;
        if (!this.getWorld().isClient) {
            if (target != null) {
                id = target.getId();
            }

            this.getDataTracker().set(TARGET_ID, id);
        }

    }

    public Entity getFollowedTarget() {
        Entity entityReference = null;
        if (this.getWorld().isClient) {
            Integer id = this.getDataTracker().get(TARGET_ID);
            if (id != null && id != 0) {
                entityReference = this.getWorld().getEntityById(id);
            }
        } else {
            entityReference = this.followedTarget;
        }

        return entityReference;
    }

    public boolean shouldRenderAtSqrDistance(double distance) {
        double d0 = this.getBoundingBox().getAverageSideLength() * 4.0D;
        if (Double.isNaN(d0)) {
            d0 = 4.0D;
        }

        d0 *= 128.0D;
        boolean result = distance < d0 * d0;
        return result;
    }

    public SpellProjectile.Behaviour behaviour() {
        String string = this.getDataTracker().get(BEHAVIOUR);
        return string != null && !string.isEmpty() ? SpellProjectile.Behaviour.valueOf(string) : SpellProjectile.Behaviour.FLY;
    }
}
