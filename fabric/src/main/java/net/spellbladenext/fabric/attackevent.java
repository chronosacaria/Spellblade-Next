package net.spellbladenext.fabric;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.spellbladenext.blocks.blockentities.NetherPortalFrame;
import net.spellbladenext.interfaces.IPiglinSummon;

import java.util.Optional;

public class attackevent implements IPiglinSummon {
    public int tickCount;
    public boolean done = false;
    Level level;
    PlayerEntity playerEntity;
    boolean firstTick;

    public attackevent(Level world, Player serverPlayer) {
        this.tickCount = 0;
        this.level = world;
        this.player = serverPlayer;
        this.firstTick = true;
    }

    @Override
    public void tick() {
        if(this.tickCount % 5 == 0){
            Optional<NetherPortalFrame> frame = IPiglinSummon.summonNetherPortal(this.level,this.player,false);
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
