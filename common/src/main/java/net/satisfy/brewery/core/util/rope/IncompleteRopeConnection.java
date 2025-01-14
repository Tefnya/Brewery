package net.satisfy.brewery.core.util.rope;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.world.entity.Entity;
import net.satisfy.brewery.core.block.entity.rope.RopeKnotEntity;

@Environment(EnvType.CLIENT)
public class IncompleteRopeConnection {
    public final RopeKnotEntity from;

    public final int toId;

    private boolean alive = true;

    public IncompleteRopeConnection(RopeKnotEntity from, int toId) {
        this.from = from;
        this.toId = toId;
    }

    public boolean tryCompleteOrRemove() {
        if (isDead()) return true;
        Entity secondary = from.level().getEntity(toId);
        if (secondary == null) return false;
        RopeConnection.create(from, secondary);
        return true;
    }

    public boolean isDead() {
        return !alive || this.from.isRemoved();
    }

    public void destroy() {
        if (!alive) return;
        this.alive = false;
    }
}
