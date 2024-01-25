package net.satisfy.brewery.block.brew_event;

import net.satisfy.brewery.entity.BrewstationBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.phys.Vec3;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.client.Minecraft;  // Import the Minecraft class
import net.satisfy.brewery.registry.SoundEventRegistry;

import java.util.Set;

public abstract class BrewEvent {

    private int timeLeft;
    private boolean isSoundPlaying;

    public void tick(BrewstationBlockEntity entity) {
        onTick(entity);
        timeLeft--;

        if (timeLeft % 20 == 0) {
            playSound(entity.getLevel(), entity.getBlockPos().getX() + 0.5, entity.getBlockPos().getY() + 0.5, entity.getBlockPos().getZ() + 0.5);
            isSoundPlaying = true;
        } else if (isSoundPlaying) {
            stopSound(entity.getLevel());
            isSoundPlaying = false;
        }
    }

    public int getTimeLeft() {
        return timeLeft;
    }

    public void setTimeForEvent(int timeLeft) {
        this.timeLeft = timeLeft;
    }

    public abstract CompoundTag save(CompoundTag compoundTag);

    public abstract void load(CompoundTag compoundTag);

    public void onTick(BrewstationBlockEntity entity) {

    }

    protected BrewEvent() {
        this(0);
    }

    protected BrewEvent(int time) {
        timeLeft = time;
        isSoundPlaying = false;
    }

    public abstract void start(Set<BlockPos> components, Level level);

    public abstract boolean isFinish(Set<BlockPos> components, Level level);

    public void finish(Set<BlockPos> components, Level level) {
        stopSound(level);
    }

    private void playSound(LevelAccessor level, double x, double y, double z) {
        level.playSound(null, new BlockPos(x, y, z), SoundEventRegistry.BREWSTATION_TIMER_LOOP.get(), SoundSource.BLOCKS, 1.0F, 1.0F);
    }


    private void stopSound(LevelAccessor level) {
        Minecraft.getInstance().getSoundManager().stop(null, SoundSource.BLOCKS);
    }
}
