package net.satisfy.brewery.registry;

import dev.architectury.platform.Platform;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.Registrar;
import dev.architectury.registry.registries.RegistrySupplier;
import net.satisfy.brewery.Brewery;
import net.satisfy.brewery.effect.*;
import net.satisfy.brewery.event.RenewingTouchEvent;
import net.satisfy.brewery.util.BreweryIdentifier;
import net.minecraft.core.Registry;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;

import java.util.function.Supplier;

public class MobEffectRegistry {

    private static final DeferredRegister<MobEffect> MOB_EFFECTS = DeferredRegister.create(Brewery.MOD_ID, Registry.MOB_EFFECT_REGISTRY);
    private static final Registrar<MobEffect> MOB_EFFECTS_REGISTRAR = MOB_EFFECTS.getRegistrar();

    public static final RegistrySupplier<MobEffect> DRUNK;
    public static final RegistrySupplier<MobEffect> BLACKOUT;
    public static final RegistrySupplier<MobEffect> SATURATED;
    public static final RegistrySupplier<MobEffect> RENEWINGTOUCH;
    public static final RegistrySupplier<MobEffect> TOXICTOUCH;
    public static final RegistrySupplier<MobEffect> HEALINGTOUCH;
    public static final RegistrySupplier<MobEffect> PROTECTIVETOUCH;
    public static final RegistrySupplier<MobEffect> HARDDRINKING;
    public static final RegistrySupplier<MobEffect> PARTYSTARTER;


    private static RegistrySupplier<MobEffect> registerEffect(String name, Supplier<MobEffect> effect){
        if(Platform.isForge()){
            return MOB_EFFECTS.register(name, effect);
        }
        return MOB_EFFECTS_REGISTRAR.register(new BreweryIdentifier(name), effect);
    }

    public static void init(){
        Brewery.LOGGER.debug("Mob effects");
        MOB_EFFECTS.register();
    }

    static {
        DRUNK = registerEffect("drunk", DrunkEffect::new);
        HARDDRINKING = registerEffect("harddrinking", HarddrinkingEffect::new);
        BLACKOUT = registerEffect("blackout", () -> new BlackoutEffect().setFactorDataFactory(() -> new MobEffectInstance.FactorData(22)));
        SATURATED = registerEffect("saturated", SaturatedEffect::new);
        TOXICTOUCH = registerEffect("toxictouch", () -> new ToxicTouchEffect(MobEffectCategory.BENEFICIAL, 0x90F891));
        RENEWINGTOUCH = registerEffect("renevingtouch", () -> new RenewingTouchEffect(MobEffectCategory.BENEFICIAL, 0x90F891));
        HEALINGTOUCH = registerEffect("healingtouch", () -> new HealingTouchEffect(MobEffectCategory.BENEFICIAL, 0x90F891));
        PROTECTIVETOUCH = registerEffect("protectivetouch", () -> new ProtectiveTouchEffect(MobEffectCategory.BENEFICIAL, 0x90F891));
        PARTYSTARTER = registerEffect("partystarter", () -> new PartystarterEffect(MobEffectCategory.BENEFICIAL, 0x90F891));
    }
}
