package net.spellbladenext.effects;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.spell_power.api.MagicSchool;
import net.spell_power.api.SpellPower;
import net.spellbladenext.SpellbladeNext;
import net.spellbladenext.entities.CleansingFlameEntity;
import net.spellbladenext.items.FriendshipBracelet;

import java.util.List;

public class CleansingFlame extends StatusEffect {

    public CleansingFlame(StatusEffectCategory statusEffectCategory, int color) {
        super(statusEffectCategory, color);
    }

    @Override
    public void applyUpdateEffect(LivingEntity entity, int amplifier) {

        World world = entity.getWorld();
        if(entity instanceof PlayerEntity playerEntity) {
            List<LivingEntity> entities = world.getEntitiesByClass(LivingEntity.class, entity.getBoundingBox().expand(4D), asdf -> asdf != entity && FriendshipBracelet.PlayerFriendshipPredicate(playerEntity, asdf) && asdf.isAttackable() && !asdf.isInvulnerable());

            entities.removeIf(Entity::isInvulnerable);
            entities.removeIf(asdf -> !entity.canSee(asdf));

            Object[] entitiesArray = entities.toArray();
            int iii = 0;
            for (Entity entity2 : entities) {

                CleansingFlameEntity flux = new CleansingFlameEntity(SpellbladeNext.CLEANSING_FLAME_ENTITY_ENTITY_TYPE, world);
                flux.target = entity2;
                flux.setOwner(entity);
                flux.setPosition(
                        entity
                                .getBoundingBox()
                                .getCenter()
                                .add(
                                        new Vec3d(
                                                (entity2.getX() - entity.getX()) * entity.getBoundingBox().getXLength() / (entity2.distanceTo(entity)),
                                                (entity2.getY() - entity.getY()) * entity.getBoundingBox().getYLength() / (entity2.distanceTo(entity)),
                                                (entity2.getZ() - entity.getZ()) * entity.getBoundingBox().getZLength() / (entity2.distanceTo(entity))
                                        )
                                )
                );
                SpellPower.Result power = SpellPower.getSpellPower(MagicSchool.FIRE, entity);
                flux.power = power;
                if (!world.isClient()) {
                    entity2.getWorld().spawnEntity(flux);
                }
                //entities.remove(entity2);

            }
        }
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return duration% 5 == 4;
    }
}
