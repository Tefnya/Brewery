package net.satisfy.brewery.core.registry;

import dev.architectury.registry.level.entity.EntityAttributeRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.satisfy.brewery.Brewery;
import net.satisfy.brewery.core.block.entity.*;
import net.satisfy.brewery.core.block.entity.rope.HangingRopeEntity;
import net.satisfy.brewery.core.block.entity.rope.RopeCollisionEntity;
import net.satisfy.brewery.core.block.entity.rope.RopeKnotEntity;
import net.satisfy.brewery.core.entity.BeerElementalAttackEntity;
import net.satisfy.brewery.core.entity.BeerElementalEntity;
import net.satisfy.brewery.core.entity.DarkBrewEntity;
import net.satisfy.brewery.core.util.BreweryIdentifier;

import java.util.HashSet;
import java.util.function.Supplier;

public class EntityTypeRegistry {
    private static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(Brewery.MOD_ID, Registries.BLOCK_ENTITY_TYPE);
    private static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(Brewery.MOD_ID, Registries.ENTITY_TYPE);

    public static final RegistrySupplier<EntityType<DarkBrewEntity>> DARK_BREW = registerEntityType("dark_brew", () -> EntityType.Builder.<DarkBrewEntity>of(DarkBrewEntity::new, MobCategory.MISC).sized(0.5f, 0.5f).build(new BreweryIdentifier("dark_brew").toString()));
    public static final RegistrySupplier<EntityType<RopeKnotEntity>> ROPE_KNOT = registerEntityType("rope_knot", () -> EntityType.Builder.of(RopeKnotEntity::new, MobCategory.MISC).sized(6 / 16F, 4 / 16F).clientTrackingRange(20).canSpawnFarFromPlayer().fireImmune().build(new BreweryIdentifier("rope_knot").toString()));
    public static final RegistrySupplier<EntityType<RopeCollisionEntity>> ROPE_COLLISION = registerEntityType("rope_collision", () -> EntityType.Builder.of(RopeCollisionEntity::new, MobCategory.MISC).sized(4 / 16f, 4 / 16f).clientTrackingRange(10).noSave().noSummon().fireImmune().build(new BreweryIdentifier("rope_collision").toString()));
    public static final RegistrySupplier<EntityType<HangingRopeEntity>> HANGING_ROPE = registerEntityType("hanging_rope", () -> EntityType.Builder.of(HangingRopeEntity::new, MobCategory.MISC).sized(4 / 16f, 4 / 16f).clientTrackingRange(10).noSave().noSummon().fireImmune().build(new BreweryIdentifier("hanging_rope").toString()));
    public static final RegistrySupplier<EntityType<BeerElementalEntity>> BEER_ELEMENTAL = registerEntityType("beer_elemental", () -> EntityType.Builder.of(BeerElementalEntity::new, MobCategory.MONSTER).sized(1.0F, 1.6F).clientTrackingRange(80).updateInterval(3).build(new BreweryIdentifier("beer_elemental").toString()));
    public static final RegistrySupplier<EntityType<BeerElementalAttackEntity>> BEER_ELEMENTAL_ATTACK = registerEntityType("beer_elemental_attack", () -> EntityType.Builder.<BeerElementalAttackEntity>of(BeerElementalAttackEntity::new, MobCategory.MISC).sized(0.3125F, 0.3125F).clientTrackingRange(4).updateInterval(10).build(new BreweryIdentifier("beer_elemental_attack").toString()));

    public static final RegistrySupplier<BlockEntityType<BrewstationBlockEntity>> BREWINGSTATION_BLOCK_ENTITY = registerBlockEntity("brewingstation", () -> BlockEntityType.Builder.of(BrewstationBlockEntity::new, ObjectRegistry.WOODEN_BREWINGSTATION.get(), ObjectRegistry.COPPER_BREWINGSTATION.get(), ObjectRegistry.NETHERITE_BREWINGSTATION.get()).build(null));
    public static final RegistrySupplier<BlockEntityType<BeerMugBlockEntity>> BEER_MUG_BLOCK_ENTITY = registerBlockEntity("beer_mug", () -> BlockEntityType.Builder.of(BeerMugBlockEntity::new, ObjectRegistry.BEER_MUG.get()).build(null));
    public static final RegistrySupplier<BlockEntityType<StorageBlockEntity>> STORAGE_ENTITY = registerBlockEntity("storage", () -> BlockEntityType.Builder.of(net.satisfy.brewery.core.block.entity.StorageBlockEntity::new, StorageTypeRegistry.registerBlocks(new HashSet<>()).toArray(new Block[0])).build(null));
    public static final RegistrySupplier<BlockEntityType<CabinetBlockEntity>> CABINET_BLOCK_ENTITY = registerBlockEntity("cabinet", () -> BlockEntityType.Builder.of(CabinetBlockEntity::new, StorageTypeRegistry.registerBlocks(new HashSet<>()).toArray(new Block[0])).build(null));
    public static final RegistrySupplier<BlockEntityType<SideBoardBlockEntity>> SIDEBOARD_BLOCK_ENTITY = registerBlockEntity("sideboard", () -> BlockEntityType.Builder.of(SideBoardBlockEntity::new, ObjectRegistry.SIDEBOARD.get()).build(null));
    public static final RegistrySupplier<BlockEntityType<CompletionistBannerEntity>> BREWERY_BANNER = registerBlockEntity("brewery_banner", () -> BlockEntityType.Builder.of(CompletionistBannerEntity::new, ObjectRegistry.BREWERY_BANNER.get(), ObjectRegistry.BREWERY_WALL_BANNER.get()).build(null));

    public static void registerBeerElemental(Supplier<? extends EntityType<? extends Monster>> typeSupplier) {
        EntityAttributeRegistry.register(typeSupplier, BeerElementalEntity::createAttributes);
    }

    private static <T extends BlockEntityType<?>> RegistrySupplier<T> registerBlockEntity(final String path, final Supplier<T> type) {
        return BLOCK_ENTITY_TYPES.register(new BreweryIdentifier(path), type);
    }

    private static <T extends EntityType<?>> RegistrySupplier<T> registerEntityType(final String path, final Supplier<T> type) {
        return ENTITY_TYPES.register(new BreweryIdentifier(path), type);
    }

    public static void init() {
        ENTITY_TYPES.register();
        BLOCK_ENTITY_TYPES.register();
        registerBeerElemental(BEER_ELEMENTAL);
    }
}
