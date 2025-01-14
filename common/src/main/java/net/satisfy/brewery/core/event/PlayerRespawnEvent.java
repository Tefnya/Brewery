package net.satisfy.brewery.core.event;

import dev.architectury.event.events.common.PlayerEvent;
import net.minecraft.server.level.ServerPlayer;
import net.satisfy.brewery.core.effect.alcohol.AlcoholManager;
import net.satisfy.brewery.core.effect.alcohol.AlcoholPlayer;

public class PlayerRespawnEvent implements PlayerEvent.PlayerRespawn {

    @Override
    public void respawn(ServerPlayer newPlayer, boolean conqueredEnd) {
        if (newPlayer instanceof AlcoholPlayer alcoholPlayer) {
            AlcoholManager.syncAlcohol(newPlayer, alcoholPlayer.brewery$getAlcohol());
        }
    }
}
