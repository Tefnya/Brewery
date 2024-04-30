package net.satisfy.brewery.client;

import de.cristelknight.doapi.common.registry.DoApiBlocks;
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
import net.satisfy.brewery.client.model.BeerElementalModel;
import net.satisfy.brewery.client.model.RopeKnotEntityModel;
import net.satisfy.brewery.client.render.*;
import net.satisfy.brewery.event.PlayerJoinEvent;
import net.satisfy.brewery.item.ItemPredicate;
import net.satisfy.brewery.networking.BreweryNetworking;
import net.satisfy.brewery.registry.ArmorRegistry;
import net.satisfy.brewery.registry.BlockEntityRegistry;
import net.satisfy.brewery.registry.EntityRegistry;
import net.satisfy.brewery.registry.ModelRegistry;
import net.satisfy.brewery.util.rope.RopeHelper;

import static net.satisfy.brewery.registry.ObjectRegistry.*;

@Environment(EnvType.CLIENT)
public class BreweryClient {

    public static void onInitializeClient() {
        BreweryNetworking.registerS2CPackets();
        ItemPredicate.register();
        registerModelLayers();
        registerRenderer();

        RenderTypeRegistry.register(RenderType.cutout(),
                WILD_HOPS.get(), BEER_MUG.get(), BEER_WHEAT.get(), BEER_HOPS.get(), BEER_BARLEY.get(), BEER_HALEY.get(), BEER_OAT.get(), BEER_NETTLE.get(),
                HOPS_CROP_BODY.get(), HOPS_CROP.get(), WHISKEY_MAGGOALLAN.get(), WHISKEY_CARRASCONLABEL.get(), WHISKEY_LILITUSINGLEMALT.get(),
                WHISKEY_JOJANNIK.get(), WHISKEY_MAGGOALLAN.get(), WHISKEY_CRISTELWALKER.get(), DoApiBlocks.WALL_STANDARD.get(), WHISKEY_AK.get(),
                DoApiBlocks.STANDARD.get(), WHISKEY_HIGHLAND_HEARTH.get(), WHISKEY_JAMESONS_MALT.get(), WHISKEY_SMOKEY_REVERIE.get()
        );


        ColorHandlerRegistry.registerBlockColors((state, world, pos, tintIndex) -> {
                    if (world == null || pos == null) {
                        return -1;
                    }
                    return BiomeColors.getAverageWaterColor(world, pos);
        }, WOODEN_BREWINGSTATION, COPPER_BREWINGSTATION, NETHERITE_BREWINGSTATION);

        ClientStorageTypes.init();

        ClientPlayerEvent.CLIENT_PLAYER_JOIN.register(new PlayerJoinEvent());
        ClientTickEvent.CLIENT_LEVEL_PRE.register((clientLevel) -> RopeHelper.tick());
    }

    public static void preInitClient(){
        registerEntityModelLayers();
    }

    private static void registerRenderer() {
        BlockEntityRendererRegistry.register(BlockEntityRegistry.BEER_MUG_BLOCK_ENTITY.get(), BeerMugBlockEntityRenderer::new);
        BlockEntityRendererRegistry.register(BlockEntityRegistry.BREWINGSTATION_BLOCK_ENTITY.get(), BrewingstationRenderer::new);
        EntityModelLayerRegistry.register(ModelRegistry.ROPE_KNOT, RopeKnotEntityModel::createBodyLayer);
        EntityRendererRegistry.register(EntityRegistry.ROPE_KNOT, RopeKnotRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.HANGING_ROPE, HangingRopeRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.ROPE_COLLISION, RopeCollisionEntityRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.BEER_ELEMENTAL, BeerElementalRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.BEER_ELEMENTAL_ATTACK, BeerElementalAttackRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.DARK_BREW, ThrownItemRenderer::new);
    }

    public static void registerModelLayers() {
        EntityModelLayerRegistry.register(BeerElementalModel.BEER_ELEMENTAL_MODEL_LAYER, BeerElementalModel::createBodyLayer);
    }

    public static LocalPlayer getPlayer() {
        return Minecraft.getInstance().player;
    }

    public static void registerEntityModelLayers(){
        ArmorRegistry.registerArmorModelLayers();
    }
}
