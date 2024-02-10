package net.satisfy.brewery.forge;

import dev.architectury.platform.forge.EventBuses;
import dev.architectury.registry.client.level.entity.EntityRendererRegistry;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.satisfy.brewery.Brewery;
import net.satisfy.brewery.client.render.*;
import net.satisfy.brewery.forge.registry.BreweryForgeVillagers;
import net.satisfy.brewery.registry.CompostablesRegistry;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.satisfy.brewery.registry.EntityRegistry;

@Mod(Brewery.MOD_ID)
public class BreweryForge {
    public BreweryForge() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        EventBuses.registerModEventBus(Brewery.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());
        Brewery.init();
        modEventBus.addListener(this::commonSetup);
        DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> this::clientSetup);
        BreweryForgeVillagers.register(modEventBus);
    }

    private void clientSetup() {
        EntityRendererRegistry.register(EntityRegistry.BEER_ELEMENTAL, BeerElementalRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.BEER_ELEMENTAL_ATTACK, BeerElementalAttackRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.HANGING_ROPE, HangingRopeRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.ROPE_COLLISION, RopeCollisionEntityRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.ROPE_KNOT, RopeKnotRenderer::new);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(CompostablesRegistry::init);
        Brewery.commonSetup();
    }

}
