package net.spellbladenext.fabric;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import java.util.Optional;

public class attackevent implements piglinsummon {
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
            Optional<netherPortalFrame> frame = piglinsummon.summonNetherPortal(this.level,this.player,false);
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
