package net.spellbladenext.fabric;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.spell_engine.api.spell.Spell;
import net.spell_engine.internals.SpellCasterEntity;
import net.spell_engine.internals.SpellRegistry;
import net.spell_power.api.attributes.SpellAttributes;
import net.spellbladenext.ClientMod;
import net.spellbladenext.SpellbladeNext;
import net.spellbladenext.entities.renderers.CivilizedPiglinRenderer;
import net.spellbladenext.entities.renderers.ColdRenderer;
import net.spellbladenext.entities.models.AmethystModel;
import net.spellbladenext.entities.models.IcicleModel;
import net.spellbladenext.entities.renderers.*;
import net.spellbladenext.items.Orbs;
import net.spellbladenext.items.armoritems.Armors;
import net.spellbladenext.items.armoritems.InquisitorSet;
import net.spellbladenext.items.armoritems.Robes;
import net.spellbladenext.items.renderers.*;
import software.bernie.geckolib3.renderers.geo.GeoArmorRenderer;
import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;

import java.util.Objects;

import static net.spellbladenext.SpellbladeNext.MOD_ID;

public class ExampleModFabricClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ClientMod.initialize();
        ClientTickEvents.START_CLIENT_TICK.register(server -> {
            PlayerEntity playerEntity = server.player;
            Level level = server.level;
            if (player != null && level != null && !player.isShiftKeyDown()) {
                double speed = player.getAttributeValue(Attributes.MOVEMENT_SPEED) * player.getAttributeValue(SpellAttributes.HASTE.attribute)*0.01 * 4;
                BlockHitResult result = level.clip(new ClipContext(player.position(), player.position().add(0, -2, 0), ClipContext.Block.COLLIDER, ClipContext.Fluid.ANY, player));
                double modifier = 0;
                if (result.getType() == HitResult.Type.BLOCK) {
                    modifier = 1;
                }

                if (SpellRegistry.getSpell(new ResourceLocation(MOD_ID, "maelstrom")) != null) {
                    Spell spell = SpellRegistry.getSpell(new ResourceLocation(MOD_ID, "maelstrom"));

                    if (player instanceof SpellCasterEntity caster) {
                        if (Objects.equals(caster.getCurrentSpellId(), new ResourceLocation(MOD_ID, "maelstrom"))) {

                            player.setDeltaMovement(player.getViewVector(1).subtract(0, player.getViewVector(1).y, 0).normalize().multiply(speed, speed * modifier, speed).add(0,player.getDeltaMovement().y,0));
                        }
                    }
                }
                if (SpellRegistry.getSpell(new ResourceLocation(MOD_ID, "inferno")) != null) {
                    Spell spell = SpellRegistry.getSpell(new ResourceLocation(MOD_ID, "inferno"));
                    if (player instanceof SpellCasterEntity caster) {

                        if (Objects.equals(caster.getCurrentSpellId(), new ResourceLocation(MOD_ID, "inferno"))) {


                            player.setDeltaMovement(player.getViewVector(1).subtract(0, player.getViewVector(1).y, 0).normalize().multiply(speed, speed * modifier, speed).add(0,player.getDeltaMovement().y,0));
                        }
                    }
                }
                if (SpellRegistry.getSpell(new ResourceLocation(MOD_ID, "cyclone")) != null) {

                    Spell spell = SpellRegistry.getSpell(new ResourceLocation(MOD_ID, "cyclone"));
                    if (player instanceof SpellCasterEntity caster) {
                        if (Objects.equals(caster.getCurrentSpellId(), new ResourceLocation(MOD_ID, "cyclone"))) {


                            player.setDeltaMovement(player.getViewVector(1).subtract(0, player.getViewVector(1).y, 0).normalize().multiply(speed, speed * modifier, speed).add(0,player.getDeltaMovement().y,0));
                        }
                    }
                }
            }

        });
        GeoItemRenderer.registerItemRenderer(Orbs.fireOrb.item(), new OrbRenderer());
        GeoItemRenderer.registerItemRenderer(Orbs.arcaneOrb.item(), new OrbRenderer());
        GeoItemRenderer.registerItemRenderer(Orbs.frostOrb.item(), new OrbRenderer());
        for (var entry: Armors.ENTRIES) {
            if(entry.armorSet().pieces().stream().allMatch(asdf -> asdf instanceof Robes)) {
                GeoArmorRenderer.registerArmorRenderer(new RobeRenderer(),
                        entry.armorSet().head,
                        entry.armorSet().chest,
                        entry.armorSet().legs,
                        entry.armorSet().feet);
                GeoItemRenderer.registerItemRenderer(entry.armorSet().head, new RobeItemRenderer());
                GeoItemRenderer.registerItemRenderer(entry.armorSet().chest, new RobeItemRenderer());
                GeoItemRenderer.registerItemRenderer(entry.armorSet().legs, new RobeItemRenderer());
                GeoItemRenderer.registerItemRenderer(entry.armorSet().feet, new RobeItemRenderer());


            }
            if(entry.armorSet().pieces().stream().allMatch(asdf -> asdf instanceof InquisitorSet)) {
                GeoArmorRenderer.registerArmorRenderer(new InquisitorRenderer(),
                        entry.armorSet().head,
                        entry.armorSet().chest,
                        entry.armorSet().legs,
                        entry.armorSet().feet);
                GeoItemRenderer.registerItemRenderer(entry.armorSet().head, new InquisitorItemRenderer());
                GeoItemRenderer.registerItemRenderer(entry.armorSet().chest, new InquisitorItemRenderer());
                GeoItemRenderer.registerItemRenderer(entry.armorSet().legs, new InquisitorItemRenderer());
                GeoItemRenderer.registerItemRenderer(entry.armorSet().feet, new InquisitorItemRenderer());


            }
        }
/*
        GeoArmorRenderer.registerArmorRenderer(new RobeRenderer(), ExampleModFabric.HOOD,
                ExampleModFabric.ROBE, ExampleModFabric.PANTS, ExampleModFabric.BOOTS);
        GeoItemRenderer.registerItemRenderer(ExampleModFabric.HOOD, new RobeItemRenderer());
        GeoItemRenderer.registerItemRenderer(ExampleModFabric.PANTS, new RobeItemRenderer());

        GeoItemRenderer.registerItemRenderer(ExampleModFabric.ROBE, new RobeItemRenderer());

        GeoItemRenderer.registerItemRenderer(ExampleModFabric.BOOTS, new RobeItemRenderer());*/

       /* FabricLoader.getInstance().getModContainer(SpellbladeNext.MOD_ID).ifPresent(modContainer -> {
            ResourceManagerHelper.registerBuiltinResourcePack(new ResourceLocation(SpellbladeNext.MOD_ID, "alternateswords"), modContainer, ResourcePackActivationType.NORMAL);
            //System.out.println("Registering Classic style resourcepack for Simply Swords");
        });*/
        EntityRendererRegistry.register(SpellbladeNext.AMETHYST, AmethystRenderer::new);
        EntityRendererRegistry.register(SpellbladeNext.AMETHYST2, AmethystRenderer::new);
        //EntityRendererRegistry.register(SpellbladeNext.AMETHYST2, AmethystRenderer::new);

        EntityRendererRegistry.register(SpellbladeNext.ICICLE_BARRIER_ENTITY_ENTITY_TYPE, IcicleRenderer::new);
        EntityRendererRegistry.register(SpellbladeNext.ICE_THORN_ENTITY_TYPE, ThrownItemRenderer::new);

        EntityRendererRegistry.register(SpellbladeNext.ENDERS_GAZE_ENTITY_ENTITY_TYPE, ThrownItemRenderer::new);
        EntityRendererRegistry.register(SpellbladeNext.ENDERS_GAZE_ENTITY_TYPE, ThrownItemRenderer::new);
        EntityRendererRegistry.register(ExampleModFabric.REAVER, CivilizedPiglinRenderer::new);
        EntityRendererRegistry.register(ExampleModFabric.MAGUS, MagusRenderer::new);

        EntityRendererRegistry.register(ExampleModFabric.SPIN, SpinRenderer::new);
        EntityRendererRegistry.register(ExampleModFabric.COLDATTACK, ColdRenderer::new);

        EntityRendererRegistry.register(ExampleModFabric.NETHERPORTAL, FallingBlockRenderer::new);
        EntityRendererRegistry.register(ExampleModFabric.NETHER_PORTAL_FRAME, FallingBlockRenderer::new);

        EntityRendererRegistry.register(SpellbladeNext.MAGMA_ORB_ENTITY_ENTITY_TYPE, (asdf) -> new ThrownItemRenderer<>(asdf,2.0F,true));
        EntityModelLayerRegistry.registerModelLayer(AmethystModel.LAYER_LOCATION, AmethystModel::createBodyLayer);
        EntityModelLayerRegistry.registerModelLayer(IcicleModel.LAYER_LOCATION, IcicleModel::createBodyLayer);

        EntityRendererRegistry.register(SpellbladeNext.CLEANSING_FLAME_ENTITY_ENTITY_TYPE, (asdf) -> new ThrownRenderer<>(asdf,2.0F,true));
        EntityRendererRegistry.register(SpellbladeNext.ERUPTION_ENTITY_TYPE, ThrownItemRenderer::new);

        EntityRendererRegistry.register(SpellbladeNext.FLAME_WINDS_ENTITY_ENTITY_TYPE, ThrownItemRenderer::new);
        EntityRendererRegistry.register(SpellbladeNext.EXPLOSION_DUMMY_ENTITY_TYPE, (asdf) -> new ThrownItemRenderer<>(asdf,3.0F,true));


    }
}
