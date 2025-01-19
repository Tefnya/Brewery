package net.satisfy.brewery.client;

import dev.architectury.event.events.client.ClientPlayerEvent;
import dev.architectury.event.events.client.ClientTickEvent;
import dev.architectury.registry.client.level.entity.EntityModelLayerRegistry;
import dev.architectury.registry.client.level.entity.EntityRendererRegistry;
import dev.architectury.registry.client.rendering.BlockEntityRendererRegistry;
import dev.architectury.registry.client.rendering.ColorHandlerRegistry;
import dev.architectury.registry.client.rendering.RenderTypeRegistry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.satisfy.brewery.client.model.*;
import net.satisfy.brewery.client.renderer.block.*;
import net.satisfy.brewery.client.renderer.entity.*;
import net.satisfy.brewery.core.event.PlayerJoinEvent;
import net.satisfy.brewery.core.item.ItemPredicate;
import net.satisfy.brewery.core.networking.BreweryNetworking;
import net.satisfy.brewery.core.registry.EntityTypeRegistry;
import net.satisfy.brewery.core.registry.ModelRegistry;
import net.satisfy.brewery.core.registry.StorageTypeRegistry;
import net.satisfy.brewery.core.util.rope.RopeHelper;

import static net.satisfy.brewery.core.registry.ObjectRegistry.*;

@Environment(EnvType.CLIENT)
public class BreweryClient {

    public static void onInitializeClient() {
        BreweryNetworking.registerS2CPackets();
        ItemPredicate.register();

        RenderTypeRegistry.register(RenderType.cutout(),
                WILD_HOPS.get(), BEER_MUG.get(), BEER_WHEAT.get(), BEER_HOPS.get(), BEER_BARLEY.get(), BEER_HALEY.get(), BEER_OAT.get(), BEER_NETTLE.get(),
                HOPS_CROP_BODY.get(), HOPS_CROP.get(), WHISKEY_MAGGOALLAN.get(), WHISKEY_CARRASCONLABEL.get(), WHISKEY_LILITUSINGLEMALT.get(),
                WHISKEY_JOJANNIK.get(), WHISKEY_MAGGOALLAN.get(), WHISKEY_CRISTELWALKER.get(), WHISKEY_AK.get(), WHISKEY_HIGHLAND_HEARTH.get(),
                WHISKEY_JAMESONS_MALT.get(), WHISKEY_SMOKEY_REVERIE.get(), BREWERY_BANNER.get(), BREWERY_WALL_BANNER.get()
        );


        ColorHandlerRegistry.registerBlockColors((state, world, pos, tintIndex) -> {
            if (world == null || pos == null) {
                return -1;
            }
            return BiomeColors.getAverageWaterColor(world, pos);
        }, WOODEN_BREWINGSTATION, COPPER_BREWINGSTATION, NETHERITE_BREWINGSTATION);

        BlockEntityRendererRegistry.register(EntityTypeRegistry.BREWERY_BANNER.get(), CompletionistBannerRenderer::new);
        BlockEntityRendererRegistry.register(EntityTypeRegistry.STORAGE_ENTITY.get(), context -> new StorageBlockEntityRenderer());
        BlockEntityRendererRegistry.register(EntityTypeRegistry.BEER_MUG_BLOCK_ENTITY.get(), BeerMugRenderer::new);
        BlockEntityRendererRegistry.register(EntityTypeRegistry.BREWINGSTATION_BLOCK_ENTITY.get(), BrewingstationRenderer::new);
        StorageBlockEntityRenderer.registerStorageType(StorageTypeRegistry.BEVERAGE, new BeverageRenderer());

        ClientPlayerEvent.CLIENT_PLAYER_JOIN.register(new PlayerJoinEvent());
        ClientTickEvent.CLIENT_LEVEL_PRE.register((clientLevel) -> RopeHelper.tick());
    }


    public static void preInitClient() {
        registerEntityRenderers();
        registerEntityModelLayers();
    }

    private static void registerEntityRenderers() {
        EntityRendererRegistry.register(EntityTypeRegistry.ROPE_KNOT, RopeKnotRenderer::new);
        EntityRendererRegistry.register(EntityTypeRegistry.HANGING_ROPE, HangingRopeRenderer::new);
        EntityRendererRegistry.register(EntityTypeRegistry.ROPE_COLLISION, RopeCollisionEntityRenderer::new);
        EntityRendererRegistry.register(EntityTypeRegistry.BEER_ELEMENTAL, BeerElementalRenderer::new);
        EntityRendererRegistry.register(EntityTypeRegistry.BEER_ELEMENTAL_ATTACK, BeerElementalAttackRenderer::new);
        EntityRendererRegistry.register(EntityTypeRegistry.DARK_BREW, ThrownItemRenderer::new);
    }

    public static void registerEntityModelLayers() {
        EntityModelLayerRegistry.register(BrewfestHatModel.LAYER_LOCATION, BrewfestHatModel::createBodyLayer);
        EntityModelLayerRegistry.register(BrewfestChestplateModel.LAYER_LOCATION, BrewfestChestplateModel::createBodyLayer);
        EntityModelLayerRegistry.register(BrewfestLeggingsModel.LAYER_LOCATION, BrewfestLeggingsModel::createBodyLayer);
        EntityModelLayerRegistry.register(BrewfestBootsModel.LAYER_LOCATION, BrewfestBootsModel::createBodyLayer);
        EntityModelLayerRegistry.register(BeerElementalModel.BEER_ELEMENTAL_MODEL_LAYER, BeerElementalModel::createBodyLayer);
        EntityModelLayerRegistry.register(ModelRegistry.ROPE_KNOT, RopeKnotEntityModel::createBodyLayer);
        EntityModelLayerRegistry.register(CompletionistBannerRenderer.LAYER_LOCATION, CompletionistBannerRenderer::createBodyLayer);
    }

    public static LocalPlayer getPlayer() {
        return Minecraft.getInstance().player;
    }

}
