package net.satisfy.brewery;

import dev.architectury.event.events.common.InteractionEvent;
import dev.architectury.event.events.common.PlayerEvent;
import dev.architectury.registry.CreativeTabRegistry;
import dev.architectury.registry.level.entity.EntityAttributeRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.satisfy.brewery.block.SiloBlock;
import net.satisfy.brewery.block.brew_event.BrewEvents;
import net.satisfy.brewery.entity.beer_elemental.BeerElementalEntity;
import net.satisfy.brewery.event.*;
import net.satisfy.brewery.event.partyeffect.ParticleSpawnEvent;
import net.satisfy.brewery.networking.BreweryNetworking;
import net.satisfy.brewery.registry.*;
import net.satisfy.brewery.util.BreweryIdentifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Brewery {
    public static final String MOD_ID = "brewery";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    public static final CreativeModeTab CREATIVE_TAB = CreativeTabRegistry.create(new BreweryIdentifier("creative_tab"), () -> new ItemStack(ObjectRegistry.BEER_MUG.get()));

    public static void init() {
        LOGGER.debug("Initiate " + MOD_ID);
        BrewEvents.loadClass();
        ObjectRegistry.init();
        BlockEntityRegistry.init();
        MobEffectRegistry.init();
        CommonEvents.init();
        BreweryNetworking.registerC2SPackets();
        SiloBlock.registerDryers();
        EntityRegistry.init();
        SoundEventRegistry.init();
        RecipeTypeRegistry.init();
        registerEvents();


        HealingTouchEvent healingTouchEvent = new HealingTouchEvent();
        PlayerEvent.ATTACK_ENTITY.register(healingTouchEvent);
        RenewingTouchEvent renewingTouchEvent = new RenewingTouchEvent();
        PlayerEvent.ATTACK_ENTITY.register(renewingTouchEvent);
        ToxicTouchEvent toxicTouchEvent = new ToxicTouchEvent();
        PlayerEvent.ATTACK_ENTITY.register(toxicTouchEvent);
        PartyStarterEvent partyStarterEvent = new PartyStarterEvent();
        PlayerEvent.ATTACK_ENTITY.register(partyStarterEvent);
        ProtectiveTouchEvent protectiveTouchEvent = new ProtectiveTouchEvent();
        PlayerEvent.ATTACK_ENTITY.register(protectiveTouchEvent);
    }

    public static void commonSetup(){
        EntityAttributeRegistry.register(EntityRegistry.BEER_ELEMENTAL, BeerElementalEntity::createAttributes);
    }

    private static void registerEvents() {
        PlayerEvent.PLAYER_RESPAWN.register(new PlayerRespawnEvent());
        PlayerEvent.PLAYER_CLONE.register(new PlayerCloneEvent());
        InteractionEvent.RIGHT_CLICK_BLOCK.register(new BlockClickEvent());
    }

    public static ResourceLocation MOD_ID(String path)
    {
        return new ResourceLocation(Brewery.MOD_ID, path);
    }
}
