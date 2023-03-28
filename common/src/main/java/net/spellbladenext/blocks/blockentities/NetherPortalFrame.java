package net.spellbladenext.blocks.blockentities;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.core.BlockPos;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.FallingBlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.World;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.spellbladenext.entities.ReaverEntity;

public class NetherPortalFrame extends FallingBlockEntity {

    public Player owner;
    public boolean ishome = false;
    public int hometicks = 0;
    public boolean goinghome = false;
    public BlockPos origin = BlockPos.ZERO;
    public NetherPortalFrame(EntityType<NetherPortalFrame> netherPortalFrameEntityType, World world) {
        super(netherPortalFrameEntityType, world);
    }


    public BlockState getBlockState() {
        return Blocks.OBSIDIAN.getDefaultState();
    }
    public float damage = 6;
    public boolean isTip;
    public NetherPortalFrame(World world, double v, double y, double v1, LivingEntity livingEntity, int size, float damage) {
        super(ExampleModFabric.NETHER_PORTAL_FRAME,world);
        this.blocksBuilding = true;
        this.setPos(v, y-5, v1);
        this.setVelocity(Vec3d.ZERO);
        this.prevX = v;
        this.prevY = y-5;
        this.prevZ = v1;
        this.setStartPos(this.blockPosition());
        if(livingEntity instanceof PlayerEntity) {
            this.setOwner((PlayerEntity) livingEntity);
        }
        this.damage = damage;
    }

    @Override
    public boolean shouldRenderName() {
        return false;
    }

    public void setOwner(PlayerEntity playerEntity){
        this.owner = playerEntity;
    }


    public NetherPortalFrame(EntityType<? extends NetherPortalFrame> p_36833_, World p_36834_, LivingEntity player, BlockPos blockPos, float damage, boolean bool, boolean home) {
        super(p_36833_, p_36834_);


        if(bool){
            NetherPortalFrame.fall(level,blockPos.above().above().above().above().west(),player,0, damage,  home);
            NetherPortalFrame.fall(level,blockPos.above().above().above().above().east(),player,0, damage,  home);
            NetherPortalFrame.fall(level,blockPos.above().above().above().above().east().east(),player,0, damage,  home);
            NetherPortalFrame.fall(level,blockPos.above().above().above().above(),player,0, damage,  home);

            NetherPortalFrame.fall(level,blockPos.above().above().above().west(),player,0, damage,  home);
            NetherPortalFrame.fall(level,blockPos.above().above().above().east().east(),player,0, damage,  home);

            NetherPortalFrame.fall(level,blockPos.above().above().west(),player,0, damage,  home);
            NetherPortalFrame.fall(level,blockPos.above().above().east().east(),player,0, damage,  home);

            NetherPortalFrame.fall(level,blockPos.above().east().east(),player,0, damage,  home);
            NetherPortalFrame.fall(level,blockPos.above().west(),player,0, damage,  home);

            NetherPortalFrame.fall(level,blockPos.west(),player,0, damage,  home);
            NetherPortalFrame.fall(level,blockPos.east(),player,0, damage,  home);
            NetherPortalFrame.fall(level,blockPos.east().east(),player,0, damage,  home);
            NetherPortalFrame.fall(level,blockPos,player,0, damage,  home);
        }
        else{
            NetherPortalFrame.fall(level,blockPos.above().above().above().above().north(),player,0, damage,  home);
            NetherPortalFrame.fall(level,blockPos.above().above().above().above().south(),player,0, damage,  home);
            NetherPortalFrame.fall(level,blockPos.above().above().above().above().south().south(),player,0, damage,  home);
            NetherPortalFrame.fall(level,blockPos.above().above().above().above(),player,0, damage,  home);

            NetherPortalFrame.fall(level,blockPos.above().above().above().north(),player,0, damage,  home);
            NetherPortalFrame.fall(level,blockPos.above().above().above().south().south(),player,0, damage,  home);

            NetherPortalFrame.fall(level,blockPos.above().above().north(),player,0, damage,  home);
            NetherPortalFrame.fall(level,blockPos.above().above().south().south(),player,0, damage,  home);

            NetherPortalFrame.fall(level,blockPos.above().south().south(),player,0, damage,  home);
            NetherPortalFrame.fall(level,blockPos.above().north(),player,0, damage,  home);

            NetherPortalFrame.fall(level,blockPos.north(),player,0, damage,  home);
            NetherPortalFrame.fall(level,blockPos.south(),player,0, damage,  home);
            NetherPortalFrame.fall(level,blockPos.south().south(),player,0, damage,  home);
            NetherPortalFrame.fall(level,blockPos,player,0, damage,  home);


        }

    }
    public static NetherPortalFrame fall(World p_201972_, BlockPos p_201973_, LivingEntity player, int size, float damage, boolean home) {
        NetherPortalFrame fallingblockentity = new NetherPortalFrame(p_201972_, (double)p_201973_.getX() + 0.5D, (double)p_201973_.getY(), (double)p_201973_.getZ() + 0.5D,player,size, damage);
        fallingblockentity.damage = damage;
        fallingblockentity.ishome = home;
        if(!p_201972_.isClientSide()) {
            p_201972_.addFreshEntity(fallingblockentity);
        }
        return fallingblockentity;
    }
    @Override
    public boolean canBeCollidedWith() {
        return true;
    }

    @Override
    public boolean canCollideWith(Entity p_20303_) {
        return true;
    }

    @Override
    public void tick() {
        if(firstTick){
            SoundEvent soundEvent = SoundEvents.BLAZE_SHOOT;
            this.playSound(soundEvent, 1F, 0.5F);
        }

        this.setNoGravity(true);
        this.noPhysics = true;
        if(tickCount < 20){
            this.setPos(this.position().add(0,6F/20F,0));
        }

        if(tickCount > 220 && !this.ishome){
            this.setPos(this.position().add(0,-6F/20F,0));

        }
        if(tickCount > 240 && !this.ishome){
            this.discard();
        }
        if(this.ishome && this.getWorld().getNearestEntity(ReaverEntity.class, TargetingConditions.forNonCombat(),null,this.origin.getX(),this.origin.getY(),this.origin.getZ(),this.getBoundingBox().inflate(32)) == null){
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
