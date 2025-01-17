package net.spellbladenext.fabric;

import com.google.common.collect.ImmutableMap;
import net.minecraft.server.level.ServerWorld;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.behavior.EntityTracker;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.NearestVisibleLivingEntities;

public class BackUp<E extends Mob> extends Behavior<E> {
    private final int tooCloseDistance;
    private final float strafeSpeed;

    public BackUp(int i, float f) {
        super(ImmutableMap.of( MemoryModuleType.LOOK_TARGET, MemoryStatus.REGISTERED, MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_PRESENT, MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES, MemoryStatus.VALUE_PRESENT));
        this.tooCloseDistance = i;
        this.strafeSpeed = f;
    }

    protected boolean checkExtraStartConditions(ServerWorld serverWorld, E mob) {
        return this.isTargetVisible(mob) && this.isTargetTooClose(mob);
    }

    protected void start(ServerWorld serverWorld, E mob, long l) {
        //System.out.println("backing up!");
        mob.getBrain().setMemory(MemoryModuleType.LOOK_TARGET, new EntityTracker(this.getTarget(mob), true));
        mob.getMoveControl().strafe(-this.strafeSpeed, 0.0F);
        mob.setYRot(Mth.rotateIfNecessary(mob.getYRot(), mob.yHeadRot, 0.0F));
    }

    private boolean isTargetVisible(E mob) {
        return ((NearestVisibleLivingEntities)mob.getBrain().getMemory(MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES).get()).contains(this.getTarget(mob));
    }

    private boolean isTargetTooClose(E mob) {
        return this.getTarget(mob).closerThan(mob, (double)this.tooCloseDistance);
    }

    private LivingEntity getTarget(E mob) {
        return (LivingEntity)mob.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).get();
    }
}
