package net.satisfy.brewery.forge.client;

import dev.architectury.registry.client.level.entity.EntityRendererRegistry;
import net.satisfy.brewery.Brewery;
import net.satisfy.brewery.client.BreweryClient;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.registries.RegisterEvent;
import net.satisfy.brewery.client.render.*;
import net.satisfy.brewery.registry.EntityRegistry;

@Mod.EventBusSubscriber(modid = Brewery.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class BreweryClientForge {

    @SubscribeEvent
    public static void beforeClientSetup(RegisterEvent event) {
        BreweryClient.preInitClient();
    }

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        EntityRendererRegistry.register(EntityRegistry.BEER_ELEMENTAL, BeerElementalRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.BEER_ELEMENTAL_ATTACK, BeerElementalAttackRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.HANGING_ROPE, HangingRopeRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.ROPE_COLLISION, RopeCollisionEntityRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.ROPE_KNOT, RopeKnotRenderer::new);
        BreweryClient.onInitializeClient();
    }

}
