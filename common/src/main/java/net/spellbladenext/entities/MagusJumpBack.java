package net.spellbladenext.entities;

import com.google.common.collect.ImmutableMap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.EntityLookTarget;
import net.minecraft.entity.ai.brain.MemoryModuleState;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.Task;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import net.spell_engine.api.spell.Spell;
import net.spell_engine.internals.SpellRegistry;
import net.spell_engine.particle.ParticleHelper;
import net.spell_engine.utils.SoundHelper;
import net.spell_power.api.MagicSchool;
import net.spell_power.api.SpellDamageSource;
import net.spellbladenext.SpellbladeNext;

import java.util.List;
import java.util.Optional;

import static net.spellbladenext.entities.MagusEntity.JUMPING;
import static net.spellbladenext.entities.MagusEntity.TIER;

public class MagusJumpBack extends Task<MagusEntity> {
    private final double tooCloseDistance;
    private final float strafeSpeed;
    float time = 0;
    boolean bool = true;

    public MagusJumpBack(double i, float f) {
        super(ImmutableMap.of(
                MemoryModuleType.LOOK_TARGET,
                MemoryModuleState.REGISTERED,
                MemoryModuleType.ATTACK_TARGET,
                MemoryModuleState.VALUE_PRESENT,
                MemoryModuleType.VISIBLE_MOBS,
                MemoryModuleState.VALUE_PRESENT)
        );
        this.tooCloseDistance = i;
        this.strafeSpeed = f;
    }

    protected boolean shouldRun(ServerWorld serverWorld, MagusEntity magusEntity) {
        return this.isTargetVisible(magusEntity) && this.isTargetTooClose(magusEntity) && magusEntity.getMaxHealth()/10 < magusEntity.damagetakensincelastthink;
    }

    protected void run(ServerWorld serverWorld, MagusEntity magusEntity, long time) {
        if(this.getTarget(magusEntity).isPresent()) {
            magusEntity.getBrain().remember(MemoryModuleType.LOOK_TARGET, new EntityLookTarget(this.getTarget(magusEntity).get(), true));
        }
        bool = serverWorld.random.nextBoolean();
    }

    @Override
    protected boolean shouldKeepRunning(ServerWorld serverWorld, MagusEntity magusEntity, long time) {
        return time <=40;
    }

    @Override
    protected void finishRunning(ServerWorld serverWorld, MagusEntity magusEntity, long time) {
        this.time = 0;
        if(this.getTarget(magusEntity).isPresent()) {
            Vec3d vec31 = new Vec3d(
                    -this.getTarget(magusEntity).get().getX() + magusEntity.getX(),
                    0,
                    -this.getTarget(magusEntity).get().getZ() + magusEntity.getZ()
            );
            Vec3d vec3d = new Vec3d(vec31.normalize().getX() * 1, 0.5,vec31.normalize().getZ() * 1 );
            magusEntity.setPosition(magusEntity.getPos().add(0,0.2,0));
            magusEntity.setOnGround(false);
            magusEntity.setVelocity(vec3d);
            magusEntity.isthinking = true;
            magusEntity.thinktime = 0;
            magusEntity.damagetakensincelastthink = 0;
            magusEntity.casting = true;
            magusEntity.getDataTracker().set(JUMPING,true);
            magusEntity.getDataTracker().set(TIER, magusEntity.getDataTracker().get(TIER) + 1);
        }

        super.stop(serverWorld, magusEntity, time);
    }

    @Override
    protected void tick(ServerWorld serverWorld, MagusEntity magusEntity, long time) {
        super.tick(serverWorld, magusEntity, time);
        int i = 1;
        if(bool){
            i = -1;
        }
        if(this.getTarget(magusEntity).isPresent()) {
            int ii = 1;
            if(this.isTargetTooClose(magusEntity)){
             ii = -1;
            }
            magusEntity.getMoveControl().strafeTo(ii, i);
            magusEntity.lookAtEntity(this.getTarget(magusEntity).get(),999,999);
        }
        if(this.time % 10 == 0) {
            if (magusEntity.getMagicSchool() == MagicSchool.ARCANE) {
                Spell spell = SpellRegistry.getSpell(new Identifier(SpellbladeNext.MOD_ID, "arcaneoverdrive"));
                if (!serverWorld.isClient()) {
                    ParticleHelper.sendBatches(magusEntity, spell.release.particles);
                    SoundHelper.playSound(serverWorld,magusEntity,spell.release.sound);

                }

                List<Entity> entities = serverWorld.getEntitiesByClass(Entity.class, magusEntity.getBoundingBox().expand(4, 2, 4), asdf -> asdf != magusEntity);
                for (Entity entity : entities) {
                    entity.damage(SpellDamageSource.mob(MagicSchool.ARCANE, magusEntity), (float) magusEntity.getAttributeValue(EntityAttributes.GENERIC_ATTACK_DAMAGE) * 0.2F);
                }
            }
            if (magusEntity.getMagicSchool() == MagicSchool.FIRE) {
                Spell spell = SpellRegistry.getSpell(new Identifier(SpellbladeNext.MOD_ID, "fireoverdrive"));
                if (!serverWorld.isClient()) {
                    ParticleHelper.sendBatches(magusEntity, spell.release.particles);
                    SoundHelper.playSound(serverWorld,magusEntity,spell.release.sound);
                }

                List<Entity> entities = serverWorld.getEntitiesByClass(Entity.class, magusEntity.getBoundingBox().expand(4, 2, 4), asdf -> asdf != magusEntity);
                for (Entity entity : entities) {
                    entity.damage(SpellDamageSource.mob(MagicSchool.FIRE, magusEntity), (float) magusEntity.getAttributeValue(EntityAttributes.GENERIC_ATTACK_DAMAGE) * 0.2F);

                }
            }
            if (magusEntity.getMagicSchool() == MagicSchool.FROST) {
                Spell spell = SpellRegistry.getSpell(new Identifier(SpellbladeNext.MOD_ID, "frostoverdrive"));
                if (!serverWorld.isClient()) {
                    ParticleHelper.sendBatches(magusEntity, spell.release.particles);
                    SoundHelper.playSound(serverWorld,magusEntity,spell.release.sound);

                }

                List<Entity> entities = serverWorld.getEntitiesByClass(Entity.class, magusEntity.getBoundingBox().expand(4, 2, 4), asdf -> asdf != magusEntity);
                for (Entity entity : entities) {
                    entity.damage(SpellDamageSource.mob(MagicSchool.FROST, magusEntity), (float) magusEntity.getAttributeValue(EntityAttributes.GENERIC_ATTACK_DAMAGE) * 0.2F);
                }
            }
        }

        this.time++;
    }

    private boolean isTargetVisible(MagusEntity mob) {
        if(this.getTarget(mob).isPresent() && mob.getBrain().getOptionalMemory(MemoryModuleType.VISIBLE_MOBS).isPresent()) {
            return mob.getBrain().getOptionalMemory(MemoryModuleType.VISIBLE_MOBS).get().contains(this.getTarget(mob).get());
        }
        return false;
    }

    private boolean isTargetTooClose(MagusEntity mob) {
        if(this.getTarget(mob).isPresent()) {
        return this.getTarget(mob).get().isInRange(mob, this.tooCloseDistance);
        }
        return false;
    }

    private Optional<LivingEntity> getTarget(MagusEntity mob) {
        return mob.getBrain().getOptionalMemory(MemoryModuleType.ATTACK_TARGET);
    }
}
