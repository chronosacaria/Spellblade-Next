package net.spellbladenext.fabric;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerWorld;
import net.minecraft.stats.StatFormatter;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.World;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.spell_engine.api.spell.Spell;
import net.spell_engine.entity.SpellProjectile;
import net.spell_engine.internals.SpellHelper;
import net.spell_engine.internals.SpellRegistry;
import net.spell_engine.utils.TargetHelper;
import net.spell_power.api.SpellPower;
import net.spellbladenext.SpellbladeNext;
import net.spellbladenext.blocks.blockentities.NetherPortal;
import net.spellbladenext.blocks.blockentities.NetherPortalFrame;
import net.spellbladenext.effects.DireHex;
import net.spellbladenext.effects.Hex;
import net.spellbladenext.entities.*;
import net.spellbladenext.blocks.Hexblade;
import net.spellbladenext.config.ItemConfig;
import net.spellbladenext.config.LootConfig;
import net.spellbladenext.entities.ColdAttackEntity;
import net.spellbladenext.events.AttackEvent;
import net.spellbladenext.fabric.items.*;
import net.spellbladenext.items.Offering;
import net.spellbladenext.items.Orbs;
import net.spellbladenext.items.PrismaticEffigy;
import net.spellbladenext.items.armoritems.Armors;
import net.spellbladenext.items.spellbladeitems.SpellbladeItems;
import net.spellbladenext.items.FriendshipBracelet;
import net.tinyconfig.ConfigManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;

import static net.minecraft.core.Registry.ENTITY_TYPE;
import static net.spell_engine.internals.SpellHelper.impactTargetingMode;
import static net.spellbladenext.SpellbladeNext.*;

public class ExampleModFabric implements ModInitializer {

    public static ArrayList<AttackEvent> attackEventArrayList = new ArrayList<>();
    public static final Item NETHERDEBUG = new DebugNetherPortal(new FabricItemSettings().group(EXAMPLE_TAB).stacksTo(1));;
    public static final ResourceLocation SINCELASTHEX = new ResourceLocation(MOD_ID, "lasthextime");
    public static final ResourceLocation HEXRAID = new ResourceLocation(MOD_ID, "hex");
    public static final EntityType<ReaverEntity> REAVER;
    public static final EntityType<MagusEntity> MAGUS;

    public static final EntityType<ColdAttackEntity> COLDATTACK;
    public static RegistrySupplier<Item> OFFERING = ITEMS.register("offering", () ->
            new Offering(new Item.Properties().tab(EXAMPLE_TAB)));
    public static RegistrySupplier<Item> PRISMATICEFFIGY = ITEMS.register("prismaticeffigy", () ->
            new PrismaticEffigy(new Item.Properties().tab(EXAMPLE_TAB)));

    public static final EntityType<SpinAttackEntity> SPIN;
    public static DeferredRegister<MobEffect> MOBEFFECTS = DeferredRegister.create(MOD_ID, Registry.MOB_EFFECT_REGISTRY);

    public static RegistrySupplier<MobEffect> HEX = MOBEFFECTS.register("hex", () ->  new Hex(MobEffectCategory.HARMFUL, 0x64329F));
    public static RegistrySupplier<MobEffect> DIREHEX = MOBEFFECTS.register("direhex", () ->  new DireHex(MobEffectCategory.HARMFUL, 0x64329F));

    public static final GameRules.Key<GameRules.BooleanValue> SHOULD_INVADE = GameRuleRegistry.register("hexbladeInvade", GameRules.Category.MOBS, GameRuleFactory.createBooleanRule(true));
/*
    public static RuneblazingArmor runeblazing_helmet = new RuneblazingArmor(ModArmorMaterials.RUNEBLAZING, EquipmentSlot.HEAD, new Item.Properties().tab(EXAMPLE_TAB), MagicSchool.FIRE);
    public static RuneblazingArmor RUNEBLAZINGCHEST = new RuneblazingArmor(ModArmorMaterials.RUNEBLAZING, EquipmentSlot.CHEST, new Item.Properties().tab(EXAMPLE_TAB), MagicSchool.FIRE);
    public static RuneblazingArmor RUNEBLAZINGLEGS = new RuneblazingArmor(ModArmorMaterials.RUNEBLAZING, EquipmentSlot.LEGS, new Item.Properties().tab(EXAMPLE_TAB), MagicSchool.FIRE);
    public static RuneblazingArmor runeblazing_feet = new RuneblazingArmor(ModArmorMaterials.RUNEBLAZING, EquipmentSlot.FEET, new Item.Properties().tab(EXAMPLE_TAB), MagicSchool.FIRE);
    public static RuneblazingArmor runefrosted_head = new RuneblazingArmor(ModArmorMaterials.RUNEFROSTED, EquipmentSlot.HEAD, new Item.Properties().tab(EXAMPLE_TAB), MagicSchool.FROST);
    public static RuneblazingArmor RUNEFROSTEDCHEST = new RuneblazingArmor(ModArmorMaterials.RUNEFROSTED, EquipmentSlot.CHEST, new Item.Properties().tab(EXAMPLE_TAB), MagicSchool.FROST);
    public static RuneblazingArmor RUNEFROSTEDLEGS = new RuneblazingArmor(ModArmorMaterials.RUNEFROSTED, EquipmentSlot.LEGS, new Item.Properties().tab(EXAMPLE_TAB), MagicSchool.FROST);
    public static RuneblazingArmor runefrosted_feet = new RuneblazingArmor(ModArmorMaterials.RUNEFROSTED, EquipmentSlot.FEET, new Item.Properties().tab(EXAMPLE_TAB), MagicSchool.FROST);
    public static RuneblazingArmor runegleaming_head = new RuneblazingArmor(ModArmorMaterials.RUNEGLEAMING, EquipmentSlot.HEAD, new Item.Properties().tab(EXAMPLE_TAB), MagicSchool.ARCANE);
    public static RuneblazingArmor RUNEGLEAMINGCHEST = new RuneblazingArmor(ModArmorMaterials.RUNEGLEAMING, EquipmentSlot.CHEST, new Item.Properties().tab(EXAMPLE_TAB), MagicSchool.ARCANE);
    public static RuneblazingArmor RUNEGLEAMINGLEGS = new RuneblazingArmor(ModArmorMaterials.RUNEGLEAMING, EquipmentSlot.LEGS, new Item.Properties().tab(EXAMPLE_TAB), MagicSchool.ARCANE);
    public static RuneblazingArmor runegleaming_feet = new RuneblazingArmor(ModArmorMaterials.RUNEGLEAMING, EquipmentSlot.FEET, new Item.Properties().tab(EXAMPLE_TAB), MagicSchool.ARCANE);
*/

/*
    public static Robes HOOD = new Robes(ModArmorMaterials.WOOL,EquipmentSlot.HEAD,new Item.Properties().tab(EXAMPLE_TAB));

    public static Robes ROBE = new Robes(ModArmorMaterials.WOOL,EquipmentSlot.CHEST,new Item.Properties().tab(EXAMPLE_TAB));
    public static Robes PANTS = new Robes(ModArmorMaterials.WOOL,EquipmentSlot.LEGS,new Item.Properties().tab(EXAMPLE_TAB));
    public static Robes BOOTS = new Robes(ModArmorMaterials.WOOL,EquipmentSlot.FEET,new Item.Properties().tab(EXAMPLE_TAB));
*/

    public static ConfigManager<ItemConfig> itemConfig;
    public static ConfigManager<LootConfig> lootConfig;


    public static EntityType<NetherPortal> NETHERPORTAL;
    public static EntityType<NetherPortalFrame> NETHER_PORTAL_FRAME;
    public static final Block HEXBLADE = new Hexblade(FabricBlockSettings.of(Material.METAL).strength(5.0F, 6.0F).requiresTool().requiresCorrectToolForDrops().sound(SoundType.METAL).noOcclusion());
    @Override
    public void onInitialize() {
        Registry.register(Registry.BLOCK, new ResourceLocation(MOD_ID,"hex"), HEXBLADE);
        Registry.register(Registry.ITEM, new ResourceLocation(MOD_ID,"hexblade"), new BlockItem(HEXBLADE, new FabricItemSettings().tab(EXAMPLE_TAB).stacksTo(1)));
        //System.out.println(Registry.BANNER_PATTERN.getTag(tag2).get().toString());
        SpellbladeNext.init();

        itemConfig  = new ConfigManager<ItemConfig>
                ("items", Default.itemConfig)
                .builder()
                .setDirectory(MOD_ID)
                .sanitize(true)
                .build();

        lootConfig = new ConfigManager<LootConfig>
                ("loot", Default.lootConfig)
                .builder()
                .setDirectory(MOD_ID)
                .sanitize(true)
                .constrain(LootConfig::constrainValues)
                .build();
        MOBEFFECTS.register();
        lootConfig.refresh();
        itemConfig.refresh();
        System.out.println(itemConfig.value.weapons);
        SpellbladeItems.register(itemConfig.value.weapons);
        Orbs.register(itemConfig.value.weapons);
        Armors.register(itemConfig.value.armor_sets);



        //Registry.register(Registry.ITEM, new ResourceLocation(SpellbladeNext.MOD_ID, "bandofpacifism"),
        //        FRIENDSHIPBRACELET);


        Registry.register(Registry.CUSTOM_STAT, "lasthextime", SINCELASTHEX);
        Registry.register(Registry.CUSTOM_STAT, "hex", HEXRAID);

        Stats.CUSTOM.get(SINCELASTHEX, StatFormatter.DEFAULT);
        Stats.CUSTOM.get(HEXRAID, StatFormatter.DEFAULT);
        Registry.register(Registry.ITEM, new ResourceLocation(MOD_ID, "debug"), NETHERDEBUG);
      /*  Registry.register(Registry.ITEM, new ResourceLocation(MOD_ID, "hood"),
                HOOD);
        Registry.register(Registry.ITEM, new ResourceLocation(MOD_ID, "robe"),
                ROBE);
        Registry.register(Registry.ITEM, new ResourceLocation(MOD_ID, "pants"),
                PANTS);
        Registry.register(Registry.ITEM, new ResourceLocation(MOD_ID, "boots"),
                BOOTS);
        Registry.register(Registry.ITEM, new ResourceLocation(MOD_ID, "runeblazing_helmet"),
                runeblazing_helmet);

        Registry.register(Registry.ITEM, new ResourceLocation(MOD_ID, "runeblazing_chest"),
                RUNEBLAZINGCHEST);
        Registry.register(Registry.ITEM, new ResourceLocation(MOD_ID, "runeblazing_legs"),
                RUNEBLAZINGLEGS);
        Registry.register(Registry.ITEM, new ResourceLocation(MOD_ID, "runeblazing_feet"),runeblazing_feet);
        Registry.register(Registry.ITEM, new ResourceLocation(MOD_ID, "runefrosted_head"), runefrosted_head);

        Registry.register(Registry.ITEM, new ResourceLocation(MOD_ID, "runefrosted_chest"),
                RUNEFROSTEDCHEST);
        Registry.register(Registry.ITEM, new ResourceLocation(MOD_ID, "runefrosted_legs"),
                RUNEFROSTEDLEGS);
        Registry.register(Registry.ITEM, new ResourceLocation(MOD_ID, "runefrosted_feet"),
                runefrosted_feet);
        Registry.register(Registry.ITEM, new ResourceLocation(MOD_ID, "runegleaming_head"),
                runegleaming_head);
        Registry.register(Registry.ITEM, new ResourceLocation(MOD_ID, "runegleaming_chest"),
                RUNEGLEAMINGCHEST);
        Registry.register(Registry.ITEM, new ResourceLocation(MOD_ID, "runegleaming_legs"),
                RUNEGLEAMINGLEGS);
        Registry.register(Registry.ITEM, new ResourceLocation(MOD_ID, "runegleaming_feet"),
                runegleaming_feet);
*/
        ServerTickEvents.START_SERVER_TICK.register(server -> {
            for(ServerPlayerEntity playerEntity : server.getPlayerList().getPlayers()){
                if (((int) (player.getWorld().getDayTime() % 24000L)) % 1200 == 0) {

                    if(player.getWorld().getGameRules().getBoolean(SHOULD_INVADE) && player.getStats().getValue(Stats.CUSTOM.get(HEXRAID)) > 0) {

                        player.awardStat(SINCELASTHEX, 1);

                        if (!player.hasEffect(HEX.get()) && player.getStats().getValue(Stats.CUSTOM.get(SINCELASTHEX)) > 10 && player.getRandom().nextFloat() < 0.01 * (player.getStats().getValue(Stats.CUSTOM.get(HEXRAID))/100F) * Math.pow((1.02930223664), player.getStats().getValue(Stats.CUSTOM.get(SINCELASTHEX)))) {
                            Optional<BlockPos> pos2 = BlockPos.findClosestMatch(player.blockPosition(),64,128,
                                    asdf -> player.getWorld().getBlockState(asdf).getBlock().equals(HEXBLADE));
                            if(pos2.isPresent() || player.getInventory().hasAnyOf(Set.of(Item.BY_BLOCK.get(HEXBLADE)))){
                                player.sendSystemMessage(Component.translatable("Your triumph is respected."));
                            }
                            else {
                                player.addEffect(new MobEffectInstance(HEX.get(), 20 * 60 * 3, 0, false, false));
                            }
                        }
                    }
                    player.getStats().setValue(player,Stats.CUSTOM.get(HEXRAID),0);
                }
            }
            attackEventArrayList.removeIf(AttackEvent -> AttackEvent.tickCount > 500 || AttackEvent.done);
            for (AttackEvent attackEvent : attackEventArrayList) {
                attackEvent.tick();
            }
        });
        HurtCallback.EVENT.register((damageSource, f) -> {
            //System.out.println("asdf");
            if(damageSource.isMagic() && damageSource.getEntity() instanceof PlayerEntity playerEntity){
                player.awardStat(HEXRAID, (int) Math.ceil(f));
            }
            return InteractionResult.PASS;
        });

        ServerTickEvents.START_SERVER_TICK.register(server -> {
            for(ServerWorld level : server.getAllWorlds()) {
                for (SpellProjectile projectile : level.getEntities(EntityTypeTest.forClass(SpellProjectile.class), asdf -> asdf instanceof SpellProjectile)) {
                    if (projectile.getSpell() != null && projectile.getOwner() instanceof PlayerEntity playerEntity && projectile.getSpell().equals(SpellRegistry.getSpell(new ResourceLocation(MOD_ID, "magic_missile")))) {
                        if (projectile.tickCount >= 20) {
                            List<LivingEntity> living = projectile.getWorld().getEntitiesOfClass(LivingEntity.class, projectile.getBoundingBox().inflate(32));
                            living.removeIf(living1 -> !living1.hasLineOfSight(projectile));
                            Predicate<Entity> selectionPredicate = (target) -> {
                                return (TargetHelper.actionAllowed(TargetHelper.TargetingMode.AREA, TargetHelper.Intent.HARMFUL, player, target)
                                        && FriendshipBracelet.PlayerFriendshipPredicate(player,target) && target instanceof LivingEntity);
                            };
                            living.removeIf(living1 -> !selectionPredicate.test(living1));
                            List<LivingEntity> targeted = new ArrayList<>();
                            if(projectile.getFollowedTarget() instanceof LivingEntity livingEntity){
                                targeted.add(livingEntity);
                                targeted.add(livingEntity);
                                targeted.add(livingEntity);
                            }
                            int iii = 0;
                            while (!living.isEmpty() && targeted.size() < 3 && iii < 3) {
                                Optional.ofNullable(projectile.getWorld().getNearestEntity(living, TargetingConditions.forNonCombat(), player, projectile.getX(), projectile.getY(), projectile.getZ()))
                                        .ifPresent(asdf -> {targeted.add(asdf); living.remove(asdf);});
                                iii++;
                            }


                            for (int i = -1; i < targeted.size()-1; i++) {
                                LivingEntity asdf = targeted.get(i+1);

                                Vec3 launchPoint = projectile.position();
                                Spell spell = SpellRegistry.getSpell(new ResourceLocation(MOD_ID, "magic_missile_shard"));
                                SpellHelper.ImpactContext context = new SpellHelper.ImpactContext(1, 1.0F, (Vec3) null, SpellPower.getSpellPower(spell.school, player), impactTargetingMode(spell));
                                SpellProjectile projectile2 = new SpellProjectile(projectile.getWorld(), player, launchPoint.x(), launchPoint.y(), launchPoint.z(), SpellProjectile.Behaviour.FLY, spell, asdf, context);
                                Spell.ProjectileData projectileData = spell.release.target.projectile;

                                float velocity = projectileData.velocity;
                                float divergence = projectileData.divergence;
                                int ii = 0;
                                if (i == 0) {
                                    ii = 1;
                                }
                                projectile2.shootFromRotation(projectile, projectile.getXRot() - ii * 90, projectile.getYRot() + i * 90, 0, velocity, divergence);


                                projectile2.range = spell.range*4;
                                projectile2.getViewXRot(projectile.getXRot());
                                projectile2.setYRot(projectile.getYRot());
                                projectile.getWorld().addFreshEntity(projectile2);
                            }
                            projectile.discard();

                        }
                    }
                }
            }
        });

        itemConfig.save();

        LootTableEvents.MODIFY.register((resourceManager, lootManager, id, tableBuilder, source) -> {
            LootHelper.configure(id, tableBuilder, lootConfig.value);
        });

    }

    static {

        SpellbladeNext.AMETHYST = Registry.register(
                ENTITY_TYPE,
                new ResourceLocation(MOD_ID, "amethyst"),
                FabricEntityTypeBuilder.<AmethystPersistentProjectileEntity>create(MobCategory.MISC, AmethystPersistentProjectileEntity::new)
                        .dimensions(EntityDimensions.fixed(0.25F, 0.25F)) // dimensions in Minecraft units of the render
                        .trackRangeBlocks(128)
                        .trackedUpdateRate(1)
                        .build()
        );
        SPIN = Registry.register(
                ENTITY_TYPE,
                new ResourceLocation(MOD_ID, "shade"),
                FabricEntityTypeBuilder.<SpinAttackEntity>create(MobCategory.MISC, SpinAttackEntity::new)
                        .dimensions(EntityDimensions.fixed(0.6F, 2F)) // dimensions in Minecraft units of the render
                        .trackRangeBlocks(128)
                        .trackedUpdateRate(1)
                        .build()
        );
        COLDATTACK = Registry.register(
                ENTITY_TYPE,
                new ResourceLocation(MOD_ID, "coldattack"),
                FabricEntityTypeBuilder.<ColdAttackEntity>create(MobCategory.MISC, ColdAttackEntity::new)
                        .dimensions(EntityDimensions.fixed(0.6F, 2F)) // dimensions in Minecraft units of the render
                        .trackRangeBlocks(128)
                        .trackedUpdateRate(1)
                        .build()
        );
        SpellbladeNext.AMETHYST_SPELL_PROJECTILE_ENTITY = Registry.register(
                ENTITY_TYPE,
                new ResourceLocation(MOD_ID, "amethyst2"),
                FabricEntityTypeBuilder.<AmethystSpellProjectileEntity>create(MobCategory.MISC, AmethystSpellProjectileEntity::new)
                        .dimensions(EntityDimensions.fixed(0.25F, 0.25F)) // dimensions in Minecraft units of the render
                        .trackRangeBlocks(128)
                        .trackedUpdateRate(1)
                        .build()
        );
        SpellbladeNext.ICICLE_BARRIER_ENTITY_ENTITY_TYPE = Registry.register(
                ENTITY_TYPE,
                new ResourceLocation(MOD_ID, "iciclebarrier"),
                FabricEntityTypeBuilder.<IcicleBarrierEntity>create(MobCategory.MISC, IcicleBarrierEntity::new)
                        .dimensions(EntityDimensions.fixed(0.25F, 0.25F)) // dimensions in Minecraft units of the render
                        .trackRangeBlocks(128)
                        .trackedUpdateRate(1)
                        .build()
        );
        REAVER = Registry.register(
                ENTITY_TYPE,
                new ResourceLocation(MOD_ID, "reaver"),
                FabricEntityTypeBuilder.<ReaverEntity>create(MobCategory.MISC, ReaverEntity::new)
                        .dimensions(EntityDimensions.fixed(1F, 2F)) // dimensions in Minecraft units of the render
                        .trackRangeBlocks(128)
                        .trackedUpdateRate(1)
                        .build()
        );
        MAGUS = Registry.register(
                ENTITY_TYPE,
                new ResourceLocation(MOD_ID, "magus"),
                FabricEntityTypeBuilder.<MagusEntity>create(MobCategory.MISC, MagusEntity::new)
                        .dimensions(EntityDimensions.fixed(1F, 2F)) // dimensions in Minecraft units of the render
                        .trackRangeBlocks(128)
                        .trackedUpdateRate(1)
                        .build()
        );
        NETHER_PORTAL_FRAME = Registry.register(
                ENTITY_TYPE,
                new ResourceLocation(MOD_ID, "netherportalframe"),
                FabricEntityTypeBuilder.<NetherPortalFrame>create(MobCategory.MISC, NetherPortalFrame::new)
                        .dimensions(EntityDimensions.fixed(1F, 1F)) // dimensions in Minecraft units of the render
                        .trackRangeBlocks(128)
                        .trackedUpdateRate(1)
                        .build()
        );
        NETHERPORTAL = Registry.register(
                ENTITY_TYPE,
                new ResourceLocation(MOD_ID, "netherportal"),
                FabricEntityTypeBuilder.<NetherPortal>create(MobCategory.MISC, NetherPortal::new)
                        .dimensions(EntityDimensions.fixed(1F, 1F)) // dimensions in Minecraft units of the render
                        .trackRangeBlocks(128)
                        .trackedUpdateRate(1)
                        .build()
        );
        SpellbladeNext.MAGMA_ORB_ENTITY_ENTITY_TYPE = Registry.register(
                ENTITY_TYPE,
                new ResourceLocation(MOD_ID, "magma"),
                FabricEntityTypeBuilder.<MagmaOrbEntity>create(MobCategory.MISC, MagmaOrbEntity::new)
                        .dimensions(EntityDimensions.fixed(1F, 1F)) // dimensions in Minecraft units of the render
                        .trackRangeBlocks(128)
                        .trackedUpdateRate(1)
                        .build()
        );
        SpellbladeNext.FLAME_WINDS_ENTITY_ENTITY_TYPE = Registry.register(
                ENTITY_TYPE,
                new ResourceLocation(MOD_ID, "flamewinds"),
                FabricEntityTypeBuilder.<FlameWindsSpellProjectile>create(MobCategory.MISC, FlameWindsSpellProjectile::new)
                        .dimensions(EntityDimensions.fixed(1F, 1F)) // dimensions in Minecraft units of the render
                        .trackRangeBlocks(128)
                        .trackedUpdateRate(1)
                        .build()
        );
        SpellbladeNext.CLEANSING_FLAME_ENTITY_ENTITY_TYPE = Registry.register(
                ENTITY_TYPE,
                new ResourceLocation(MOD_ID, "cleansingflame"),
                FabricEntityTypeBuilder.<CleansingFlameEntity>create(MobCategory.MISC, CleansingFlameEntity::new)
                        .dimensions(EntityDimensions.fixed(1F, 1F)) // dimensions in Minecraft units of the render
                        .trackRangeBlocks(128)
                        .trackedUpdateRate(1)
                        .build()
        );
        SpellbladeNext.ERUPTION_ENTITY_TYPE = Registry.register(
                ENTITY_TYPE,
                new ResourceLocation(MOD_ID, "eruption"),
                FabricEntityTypeBuilder.<EruptionSpellProjectile>create(MobCategory.MISC, EruptionSpellProjectile::new)
                        .dimensions(EntityDimensions.fixed(0.5F, 0.5F)) // dimensions in Minecraft units of the render
                        .trackRangeBlocks(128)
                        .trackedUpdateRate(1)
                        .build()
        );
        SpellbladeNext.ENDERS_GAZE_ENTITY_ENTITY_TYPE = Registry.register(
                ENTITY_TYPE,
                new ResourceLocation(MOD_ID, "gaze"),
                FabricEntityTypeBuilder.<EndersGazeSpellProjectile>create(MobCategory.MISC, EndersGazeSpellProjectile::new)
                        .dimensions(EntityDimensions.fixed(0.5F, 0.5F)) // dimensions in Minecraft units of the render
                        .trackRangeBlocks(128)
                        .trackedUpdateRate(1)
                        .build()
        );
        FabricDefaultAttributeRegistry.register(REAVER, ReaverEntity.createReaverAttributes());
        FabricDefaultAttributeRegistry.register(MAGUS, MagusEntity.createAttributes());

        FabricDefaultAttributeRegistry.register(SPIN, SpinAttackEntity.createReaverAttributes());
        FabricDefaultAttributeRegistry.register(COLDATTACK, ColdAttackEntity.createReaverAttributes());

        SpellbladeNext.ENDERS_GAZE_ENTITY_TYPE = Registry.register(
                ENTITY_TYPE,
                new ResourceLocation(MOD_ID, "gazehitter"),
                FabricEntityTypeBuilder.<EndersGazeCirclingSpellProjectile>create(MobCategory.MISC, EndersGazeCirclingSpellProjectile::new)
                        .dimensions(EntityDimensions.fixed(0.5F, 0.5F)) // dimensions in Minecraft units of the render
                        .trackRangeBlocks(128)
                        .trackedUpdateRate(1)
                        .build()
        );
        SpellbladeNext.ICE_THORN_ENTITY_TYPE = Registry.register(
                ENTITY_TYPE,
                new ResourceLocation(MOD_ID, "icethorn"),
                FabricEntityTypeBuilder.<IceThornSpellProjectile>create(MobCategory.MISC, IceThornSpellProjectile::new)
                        .dimensions(EntityDimensions.fixed(0.5F, 0.5F)) // dimensions in Minecraft units of the render
                        .trackRangeBlocks(128)
                        .trackedUpdateRate(1)
                        .build()
        );
        SpellbladeNext.EXPLOSION_DUMMY_ENTITY_TYPE = Registry.register(
                ENTITY_TYPE,
                new ResourceLocation(MOD_ID, "explosion"),
                FabricEntityTypeBuilder.<ExplosionPersistentProjectileEntity>create(MobCategory.MISC, ExplosionPersistentProjectileEntity::new)
                        .dimensions(EntityDimensions.fixed(0.5F, 0.5F)) // dimensions in Minecraft units of the render
                        .trackRangeBlocks(128)
                        .trackedUpdateRate(1)
                        .build()
        );


    }
    public static BlockHitResult getPlayerPOVHitResult(World p_41436_, Player p_41437_, ClipContext.Fluid p_41438_) {
        float f = p_41437_.getXRot();
        float f1 = p_41437_.getYRot();
        Vec3 vec3 = p_41437_.getEyePosition();
        float f2 = Mth.cos(-f1 * ((float)Math.PI / 180F) - (float)Math.PI);
        float f3 = Mth.sin(-f1 * ((float)Math.PI / 180F) - (float)Math.PI);
        float f4 = -Mth.cos(-f * ((float)Math.PI / 180F));
        float f5 = Mth.sin(-f * ((float)Math.PI / 180F));
        float f6 = f3 * f4;
        float f7 = f2 * f4;
        double d0 = 8;
        Vec3 vec31 = vec3.add((double)f6 * d0, (double)f5 * d0, (double)f7 * d0);
        return p_41436_.clip(new ClipContext(vec3, vec31, ClipContext.Block.OUTLINE, p_41438_, p_41437_));
    }
    public static Vec3 getPlayerPOVHitResultplus(World p_41436_, Player p_41437_, ClipContext.Fluid p_41438_) {
        float f = 0;
        float f1 = p_41437_.getYRot()+30;
        Vec3 vec3 = p_41437_.getEyePosition();
        float f2 = Mth.cos(-f1 * ((float)Math.PI / 180F) - (float)Math.PI);
        float f3 = Mth.sin(-f1 * ((float)Math.PI / 180F) - (float)Math.PI);
        float f4 = -Mth.cos(-f * ((float)Math.PI / 180F));
        float f5 = Mth.sin(-f * ((float)Math.PI / 180F));
        float f6 = f3 * f4;
        float f7 = f2 * f4;
        double d0 = 2;
        Vec3 vec32 = new Vec3((double)f6 * d0, (double)f5 * d0, (double)f7 * d0);
        Vec3 vec33= Vec3.ZERO;
        if(vec32.horizontalDistance() != 0) {

             vec33 = new Vec3(vec32.x, 0, vec32.z);
             vec33.normalize().multiply(d0,d0,d0);
        }
        Vec3 vec31 = vec3.add(vec33);

        return vec31;
    }
    public static Vec3 getPlayerPOVHitResultminus(World p_41436_, Player p_41437_, ClipContext.Fluid p_41438_) {
        float f = 0;
        float f1 = p_41437_.getYRot()-30;
        Vec3 vec3 = p_41437_.getEyePosition();
        float f2 = Mth.cos(-f1 * ((float)Math.PI / 180F) - (float)Math.PI);
        float f3 = Mth.sin(-f1 * ((float)Math.PI / 180F) - (float)Math.PI);
        float f4 = -Mth.cos(-f * ((float)Math.PI / 180F));
        float f5 = Mth.sin(-f * ((float)Math.PI / 180F));
        float f6 = f3 * f4;
        float f7 = f2 * f4;
        double d0 = 2;
        Vec3 vec32 = new Vec3((double)f6 * d0, (double)f5 * d0, (double)f7 * d0);
        Vec3 vec33= Vec3.ZERO;
        if(vec32.horizontalDistance() != 0) {

            vec33 = new Vec3(vec32.x, 0, vec32.z);
            vec33.normalize().multiply(d0,d0,d0);
        }
        Vec3 vec31 = vec3.add(vec33);

        return vec31;
    }
}
