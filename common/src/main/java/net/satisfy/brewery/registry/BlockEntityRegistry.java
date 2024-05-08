package net.satisfy.brewery.registry;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.satisfy.brewery.Brewery;
import net.satisfy.brewery.entity.BeerMugBlockEntity;
import net.satisfy.brewery.entity.BrewstationBlockEntity;

import java.util.function.Supplier;

public class BlockEntityRegistry {
    private static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(Brewery.MOD_ID, Registries.BLOCK_ENTITY_TYPE);

    private static <T extends BlockEntityType<?>> RegistrySupplier<T> create(final String path, final Supplier<T> type) {
        return BLOCK_ENTITY_TYPES.register(path, type);
    }    public static final RegistrySupplier<BlockEntityType<BrewstationBlockEntity>> BREWINGSTATION_BLOCK_ENTITY = create("brewingstation", () -> BlockEntityType.Builder.of(BrewstationBlockEntity::new, ObjectRegistry.WOODEN_BREWINGSTATION.get(), ObjectRegistry.COPPER_BREWINGSTATION.get(), ObjectRegistry.NETHERITE_BREWINGSTATION.get()).build(null));

    public static void init() {
        Brewery.LOGGER.debug("Registering Mod BlockEntities for " + Brewery.MOD_ID);
        BLOCK_ENTITY_TYPES.register();
    }    public static final RegistrySupplier<BlockEntityType<BeerMugBlockEntity>> BEER_MUG_BLOCK_ENTITY = create("beer_mug", () -> BlockEntityType.Builder.of(BeerMugBlockEntity::new, ObjectRegistry.BEER_MUG.get()).build(null));





}
