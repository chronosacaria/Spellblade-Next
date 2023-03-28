package net.spellbladenext.blocks.blockentities;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.NetherPortalBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.FallingBlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.chat.Component;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.NetherPortalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.phys.Vec3;
import net.spellbladenext.entities.Reaver;
import net.spellbladenext.items.spellbladeitems.SpellbladeItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class NetherPortal extends FallingBlockEntity {
    public PlayerEntity owner;
    public boolean spawn = false;
    public boolean firstPiglin = true;
    public boolean ishome = false;
    public int hometicks = 0;
    public boolean goinghome = false;
    public BlockPos origin = BlockPos.ZERO;
    public NetherPortal(EntityType<NetherPortal> netherPortalFrameEntityType, World world) {
        super(netherPortalFrameEntityType, world);
    }



    @Override
    public boolean isCollidable() {
        return false;
    }

    public BlockState getBlockState() {
        BlockState state = Blocks.NETHER_PORTAL.getDefaultState();
        state = state.with(NetherPortalBlock.AXIS, Direction.Axis.X);

        if(Objects.equals(this.getCustomName(), Text.translatable("rotated"))){
            state = state.with(NetherPortalBlock.AXIS, Direction.Axis.Z);
        }
        return state;
    }

    @Override
    public boolean isCustomNameVisible() {
        return false;
    }

    public float damage = 6;
    public boolean isTip;
    public NetherPortal(World world, double v, double y, double v1, LivingEntity livingEntity, int size, float damage) {
        super(ExampleModFabric.NETHERPORTAL,world);
        this.setPos(v, y-5, v1);

        this.setVelocity(Vec3d.ZERO);
        this.prevX = v;
        this.prevY = y-5;
        this.prevZ = v1;
        this.setFallingBlockPos(this.getBlockPos());
        if(livingEntity instanceof PlayerEntity playerEntity) {
            this.setOwner(playerEntity);
        }
        this.damage = damage;
        if(size == 1){
            this.spawn = true;
        }
    }

    public void setOwner(PlayerEntity playerEntity){
        this.owner = playerEntity;
    }


    public NetherPortal(EntityType<? extends NetherPortal> entityType, World world, LivingEntity entity, BlockPos pos, float damage, boolean bool, boolean home) {
        super(entityType, world);

        if(bool) {
            NetherPortal.fall(world, pos.up(3).east(),entity,0, damage,false, home);
            NetherPortal.fall(world, pos.up(3), entity,0, damage,false, home);

            NetherPortal.fall(world, pos.up(2), entity,0, damage,false, home);
            NetherPortal.fall(world, pos.up(2).east(),entity,0, damage,false, home);
            NetherPortal.fall(world, pos.up().east(), entity,0, damage,false, home);
            NetherPortal.fall(world, pos.up(),entity, 1, damage,false, home);
        } else {
            NetherPortal.fall(world, pos.up(3).south(),entity,0, damage, true, home);
            NetherPortal.fall(world, pos.up(3), entity,0, damage, true, home);

            NetherPortal.fall(world, pos.up(2), entity,0, damage, true, home);
            NetherPortal.fall(world, pos.up(2).south(), entity,0, damage, true ,home);
            NetherPortal.fall(world, pos.up().south(), entity,0, damage, true, home);
            NetherPortal.fall(world, pos.up(), entity,1, damage, true, home);
        }

    }
    public static void fall(World world, BlockPos blockPos, LivingEntity livingEntity, int size, float damage, boolean bool, boolean home) {
        NetherPortal fallingBlockEntity = new NetherPortal(world, (double)blockPos.getX() + 0.5D, blockPos.getY(), (double)blockPos.getZ() + 0.5D, livingEntity, size, damage);

        fallingBlockEntity.damage = damage;
        fallingBlockEntity.origin = blockPos;
        fallingBlockEntity.ishome = home;
        if(livingEntity instanceof PlayerEntity playerEntity) {
            fallingBlockEntity.setOwner(playerEntity);
        }
        if(bool)
            fallingBlockEntity.setCustomName(Text.translatable("rotated"));
        if(!world.isClient()) {
            world.spawnEntity(fallingBlockEntity);
        }
    }


    @Override
    public void tick() {

        if(this.ishome && this.spawn){
            List<Reaver> piglins = this.getWorld().getEntitiesByClass(Reaver.class,this.getBoundingBox().expand(48), piglin -> piglin.returningHome);
            for(Reaver piglin : piglins){
                piglin.getBrain().setMemory(MemoryModuleType.WALK_TARGET,new WalkTarget(this,1.4F,1));
                if(piglin.distanceTo(this) < 3){
                    piglin.discard();
                }
            }
        }
        List<Reaver> piglins = this.getWorld().getEntitiesByClass(Reaver.class,this.getBoundingBox().expand(3),piglin -> piglin.returningHome);
        for(Reaver piglin : piglins){
                piglin.discard();
        }
        this.setNoGravity(true);
        this.noPhysics = true;
        if(tickCount < 20){
            this.setPos(this.position().add(0,6F/20F,0));
        }
        else if(this.spawn && this.tickCount < 100 && this.tickCount % 10 == 5 && !this.ishome){
            ArrayList<ItemStack> spellblades = new ArrayList<ItemStack>();
            for(Item item : Registry.ITEM.stream().toList()){
                if(item instanceof SpellbladeItem){
                    spellblades.add(new ItemStack(item));
                }
            }
            ItemStack spellblade = spellblades.get(this.random.nextInt(spellblades.size()));
            Reaver piglin = new Reaver(ExampleModFabric.REAVER,this.getWorld());
            piglin.tryEquip(spellblade);
            piglin.setPos(this.position());
            if(firstPiglin){
                piglin.isleader = true;
            }
            if(this.random.nextInt(5) < 2){
                piglin.isCaster() = true;
/*
                //piglin.getBrain().removeAllBehaviors();
                piglin.getBrain().addActivityAndRemoveMemoryWhenStopped(Activity.FIGHT, 9, ImmutableList.of(new StopAttackingIfTargetInvalid<PiglinBrute>((livingEntity) -> {
                    return !isNearestValidAttackTarget(piglin, (LivingEntity) livingEntity);
                }), new RunIf<PiglinBrute>(asdf -> true, new BackUpIfTooClose<PiglinBrute>(5, 0.75F)), new SetWalkTargetFromAttackTargetIfTargetOutOfReach(1.0F), new SpellAttack<PiglinBrute,LivingEntity>(), new EraseMemoryIf<PiglinBrute>(asdf -> false, MemoryModuleType.ATTACK_TARGET)), MemoryModuleType.ATTACK_TARGET);
*/
            }

            if(this.owner != null){
                //System.out.println(this.owner);
                piglin.nemesis = this.owner;
                piglin.getBrain().setMemoryWithExpiry(MemoryModuleType.WALK_TARGET,new WalkTarget(this.owner,1.5F,5),160);

            }
            this.getLevel().addFreshEntity(piglin);
            firstPiglin = false;
        }
        if(tickCount > 220 && !this.ishome){
            this.setPos(this.position().add(0,-6F/20F,0));

        }
        if(tickCount > 240 && !this.ishome){
            this.discard();
        }
        if(this.ishome && this.getLevel().getNearestEntity(Reaver.class, TargetingConditions.forNonCombat().ignoreLineOfSight(),null,this.origin.getX(),this.origin.getY(),this.origin.getZ(),this.getBoundingBox().inflate(32)) == null){
            goinghome = true;
            this.setPos(this.position().add(0,-6F/20F,0));

        }
        if(goinghome){
            hometicks++;
        }
        if(this.hometicks > 20 && this.ishome){
            this.discard();
        }

        this.firstTick = false;

    }
}
