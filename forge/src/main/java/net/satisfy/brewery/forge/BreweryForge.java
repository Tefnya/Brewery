package net.satisfy.brewery.forge;

import dev.architectury.platform.forge.EventBuses;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.satisfy.brewery.Brewery;
import net.satisfy.brewery.core.registry.CompostablesRegistry;

@Mod(Brewery.MOD_ID)
public class BreweryForge {
    public BreweryForge() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        EventBuses.registerModEventBus(Brewery.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());
        Brewery.init();
        modEventBus.addListener(this::commonSetup);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(CompostablesRegistry::init);
    }
}

