package net.spellbladenext.events;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;
import net.spellbladenext.blocks.blockentities.NetherPortalFrame;
import net.spellbladenext.interfaces.IPiglinSummon;

import java.util.Optional;

public class AttackEvent implements IPiglinSummon {
    public int tickCount;
    public boolean done = false;
    World world;
    PlayerEntity playerEntity;
    boolean firstTick;

    public AttackEvent(World world, PlayerEntity playerEntity) {
        this.tickCount = 0;
        this.world = world;
        this.playerEntity = playerEntity;
        this.firstTick = true;
    }

    @Override
    public void tick() {
        if(this.tickCount % 5 == 0){
            Optional<NetherPortalFrame> frame = IPiglinSummon.summonNetherPortal(this.world,this.playerEntity,false);
            if(frame.isPresent()){
                this.done = true;
            }
        }

        if(this.tickCount % 80 == 0) {
        }
        this.firstTick = false;
        this.tickCount++;

    }
}
