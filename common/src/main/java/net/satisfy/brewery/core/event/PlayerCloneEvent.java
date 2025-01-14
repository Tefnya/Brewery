package net.satisfy.brewery.core.event;

import dev.architectury.event.events.common.PlayerEvent;
import net.minecraft.server.level.ServerPlayer;
import net.satisfy.brewery.core.effect.alcohol.AlcoholLevel;
import net.satisfy.brewery.core.effect.alcohol.AlcoholPlayer;

public class PlayerCloneEvent implements PlayerEvent.PlayerClone {
    @Override
    public void clone(ServerPlayer oldPlayer, ServerPlayer newPlayer, boolean wonGame) {
        if (newPlayer instanceof AlcoholPlayer newAlcoholPlayer && oldPlayer instanceof AlcoholPlayer oldAlcoholPlayer) {
            AlcoholLevel alcoholLevel = oldAlcoholPlayer.brewery$getAlcohol().copy();
            alcoholLevel.soberUp();
            newAlcoholPlayer.brewery$setAlcohol(alcoholLevel);
        }
    }
}
