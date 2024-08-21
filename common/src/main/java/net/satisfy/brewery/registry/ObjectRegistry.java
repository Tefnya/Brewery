package net.satisfy.brewery.registry;

import de.cristelknight.doapi.Util;
import de.cristelknight.doapi.common.block.*;
import de.cristelknight.doapi.common.registry.DoApiSoundEventRegistry;
import dev.architectury.core.item.ArchitecturySpawnEggItem;
import dev.architectury.registry.fuel.FuelRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.Registrar;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.food.Foods;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.PushReaction;
import net.satisfy.brewery.Brewery;
import net.satisfy.brewery.block.TableBlock;
import net.satisfy.brewery.block.*;
import net.satisfy.brewery.block.barrel.BigBarrelMainBlock;
import net.satisfy.brewery.block.barrel.BigBarrelMainHeadBlock;
import net.satisfy.brewery.block.barrel.BigBarrelRightBlock;
import net.satisfy.brewery.block.barrel.BigBarrelRightHeadBlock;
import net.satisfy.brewery.block.brewingstation.BrewKettleBlock;
import net.satisfy.brewery.block.brewingstation.BrewOvenBlock;
import net.satisfy.brewery.block.brewingstation.BrewTimerBlock;
import net.satisfy.brewery.block.brewingstation.BrewWhistleBlock;
import net.satisfy.brewery.block.property.BrewMaterial;
import net.satisfy.brewery.item.*;
import net.satisfy.brewery.item.armor.*;
import net.satisfy.brewery.util.BreweryIdentifier;
import net.satisfy.farm_and_charm.block.FoodBlock;
import net.satisfy.farm_and_charm.item.food.EffectBlockItem;
import net.satisfy.farm_and_charm.item.food.EffectItem;

import java.util.function.Consumer;
import java.util.function.Supplier;

import static net.satisfy.farm_and_charm.registry.MobEffectRegistry.SATIATION;
import static net.satisfy.farm_and_charm.registry.MobEffectRegistry.SUSTENANCE;


@SuppressWarnings("all")
public class ObjectRegistry {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Brewery.MOD_ID, Registries.ITEM);
    public static final Registrar<Item> ITEM_REGISTRAR = ITEMS.getRegistrar();
    public static final RegistrySupplier<Item> HOPS = registerItem("hops", () -> new Item(getSettings().food(Foods.APPLE)));
    public static final RegistrySupplier<Item> BREATHALYZER = registerItem("breathalyzer", () -> new Breathalyzer(getSettings()));
    public static final RegistrySupplier<Item> ROPE = registerItem("rope", () -> new RopeItem(getSettings()));
    public static final RegistrySupplier<Item> DARK_BREW = registerItem("dark_brew", () -> new DarkBrewItem(getSettings()));
    public static final RegistrySupplier<Item> SAUSAGE = registerItem("sausage", () -> new EffectItem(getFoodItemSettings(8, 0.8f, SUSTENANCE.get(), 6000), 6000, true));
    public static final RegistrySupplier<Item> PRETZEL = registerItem("pretzel", () -> new EffectItem(getFoodItemSettings(4, 0.47f, SUSTENANCE.get(), 2000), 2000, false));
    public static final RegistrySupplier<Item> BEER_ELEMENTAL_SPAWN_EGG = registerItem("beer_elemental_spawn_egg", () -> new ArchitecturySpawnEggItem(EntityRegistry.BEER_ELEMENTAL, -1, -1, getSettings()));
    public static final RegistrySupplier<Item> BREWFEST_HAT = registerItem("brewfest_hat", () -> new BrewfestHatItem(ArmorMaterialRegistry.BREWFEST_ARMOR, ArmorItem.Type.HELMET, getSettings().rarity(Rarity.EPIC), new BreweryIdentifier("textures/models/armor/brewfest_hat.png")));
    public static final RegistrySupplier<Item> BREWFEST_HAT_RED = registerItem("brewfest_hat_red", () -> new BrewfestHatItem(ArmorMaterialRegistry.BREWFEST_ARMOR, ArmorItem.Type.HELMET, getSettings().rarity(Rarity.EPIC), new BreweryIdentifier("textures/models/armor/brewfest_hat_red.png")));
    public static final RegistrySupplier<Item> BREWFEST_REGALIA = registerItem("brewfest_regalia", () -> new BrewfestChest(ArmorMaterialRegistry.BREWFEST_ARMOR, getSettings().rarity(Rarity.COMMON)));
    public static final RegistrySupplier<Item> BREWFEST_TROUSERS = registerItem("brewfest_trousers", () -> new BrewfestLegs(ArmorMaterialRegistry.BREWFEST_ARMOR, getSettings().rarity(Rarity.RARE)));
    public static final RegistrySupplier<Item> BREWFEST_BOOTS = registerItem("brewfest_boots", () -> new BrewfestBoots(ArmorMaterialRegistry.BREWFEST_ARMOR, getSettings().rarity(Rarity.RARE)));
    public static final RegistrySupplier<Item> BREWFEST_DRESS = registerItem("brewfest_dress", () -> new BrewfestLegs(ArmorMaterialRegistry.BREWFEST_DRESS, getSettings().rarity(Rarity.RARE)));
    public static final RegistrySupplier<Item> BREWFEST_BLOUSE = registerItem("brewfest_blouse", () -> new BrewfestChest(ArmorMaterialRegistry.BREWFEST_DRESS, getSettings().rarity(Rarity.COMMON)));
    public static final RegistrySupplier<Item> BREWFEST_SHOES = registerItem("brewfest_shoes", () -> new BrewfestBoots(ArmorMaterialRegistry.BREWFEST_DRESS, getSettings().rarity(Rarity.UNCOMMON)));
    public static final RegistrySupplier<Item> BEER_STANDARD = registerItem("beer_standard", () -> new BeerStandardItem(new Item.Properties().stacksTo(16).rarity(Rarity.UNCOMMON)));
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(Brewery.MOD_ID, Registries.BLOCK);
    public static final Registrar<Block> BLOCK_REGISTRAR = BLOCKS.getRegistrar();
    public static final RegistrySupplier<Block> WILD_HOPS = registerWithoutItem("wild_hops", () -> new TallFlowerBlock(BlockBehaviour.Properties.copy(Blocks.ROSE_BUSH)));
    public static final RegistrySupplier<Block> HOPS_CROP = registerWithoutItem("hops_crop", () -> new HopsCropHeadBlock(getBushSettings().randomTicks()));
    public static final RegistrySupplier<Item> HOPS_SEEDS = registerItem("hops_seeds", () -> new ItemNameBlockItem(HOPS_CROP.get(), getSettings()));
    public static final RegistrySupplier<Block> HOPS_CROP_BODY = registerWithoutItem("hops_crop_body", () -> new HopsCropBodyBlock(getBushSettings().randomTicks()));
    public static final RegistrySupplier<Block> DRIED_WHEAT = registerWithItem("dried_wheat", () -> new FacingBlock(BlockBehaviour.Properties.copy(Blocks.RED_WOOL)));
    public static final RegistrySupplier<Block> DRIED_BARLEY = registerWithItem("dried_barley", () -> new FacingBlock(BlockBehaviour.Properties.copy(Blocks.RED_WOOL)));
    public static final RegistrySupplier<Block> DRIED_CORN = registerWithItem("dried_corn", () -> new FacingBlock(BlockBehaviour.Properties.copy(Blocks.RED_WOOL)));
    public static final RegistrySupplier<Block> DRIED_OAT = registerWithItem("dried_oat", () -> new FacingBlock(BlockBehaviour.Properties.copy(Blocks.RED_WOOL)));
    public static final RegistrySupplier<Block> BENCH = registerWithItem("bench", () -> new BenchBlock(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS)));
    public static final RegistrySupplier<Block> TABLE = registerWithItem("table", () -> new TableBlock(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS)));
    public static final RegistrySupplier<Block> PATTERNED_WOOL = registerWithItem("patterned_wool", () -> new Block(BlockBehaviour.Properties.copy(Blocks.BLACK_WOOL)));
    public static final RegistrySupplier<Block> PATTERNED_CARPET_BLOCK = registerWithItem("patterned_carpet_block", () -> new CarpetBlock(BlockBehaviour.Properties.copy(Blocks.BLACK_CARPET)));
    public static final RegistrySupplier<Item> PATTERNED_CARPET = registerItem("patterned_carpet", () -> new BlockItem(PATTERNED_CARPET_BLOCK.get(), getSettings()));
    public static final RegistrySupplier<Block> CABINET = registerWithItem("cabinet", () -> new CabinetBlock(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS).strength(2.0F, 3.0F).sound(SoundType.WOOD), DoApiSoundEventRegistry.CABINET_OPEN, DoApiSoundEventRegistry.CABINET_CLOSE));
    public static final RegistrySupplier<Block> DRAWER = registerWithItem("drawer", () -> new CabinetBlock(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS).strength(2.0F, 3.0F).sound(SoundType.WOOD), DoApiSoundEventRegistry.DRAWER_OPEN, DoApiSoundEventRegistry.DRAWER_CLOSE));
    public static final RegistrySupplier<Block> BAR_COUNTER = registerWithItem("bar_counter", () -> new BarCounterBlock(BlockBehaviour.Properties.of().strength(2.0F, 3.0F).sound(SoundType.WOOD).noOcclusion()));
    public static final RegistrySupplier<Block> SIDEBOARD = registerWithItem("sideboard", () -> new SideBoardBlock(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS).strength(2.5f).sound(SoundType.WOOD)));
    public static final RegistrySupplier<Block> WALL_CABINET = registerWithItem("wall_cabinet", () -> new CabinetWallBlock(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS).strength(2.0F, 3.0F).sound(SoundType.WOOD), DoApiSoundEventRegistry.CABINET_OPEN, DoApiSoundEventRegistry.CABINET_CLOSE));
    public static final RegistrySupplier<Block> WOODEN_BREWINGSTATION = registerWithItem("wooden_brewingstation", () -> new BrewKettleBlock(BrewMaterial.WOOD, BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS).pushReaction(PushReaction.IGNORE)));
    public static final RegistrySupplier<Block> COPPER_BREWINGSTATION = registerWithItem("copper_brewingstation", () -> new BrewKettleBlock(BrewMaterial.COPPER, BlockBehaviour.Properties.copy(Blocks.COPPER_BLOCK).pushReaction(PushReaction.IGNORE)));
    public static final RegistrySupplier<Block> NETHERITE_BREWINGSTATION = registerWithItem("netherite_brewingstation", () -> new BrewKettleBlock(BrewMaterial.NETHERITE, BlockBehaviour.Properties.copy(Blocks.NETHERITE_BLOCK).pushReaction(PushReaction.IGNORE)));
    public static final RegistrySupplier<Block> BREW_WHISTLE = registerWithoutItem("brew_whistle", () -> new BrewWhistleBlock(BlockBehaviour.Properties.copy(Blocks.COPPER_BLOCK).pushReaction(PushReaction.IGNORE)));
    public static final RegistrySupplier<Block> BREW_OVEN = registerWithoutItem("brew_oven", () -> new BrewOvenBlock(BlockBehaviour.Properties.copy(Blocks.COPPER_BLOCK).pushReaction(PushReaction.IGNORE)));
    public static final RegistrySupplier<Block> BREW_TIMER = registerWithoutItem("brew_timer", () -> new BrewTimerBlock(BlockBehaviour.Properties.copy(Blocks.COPPER_BLOCK).pushReaction(PushReaction.IGNORE)));
    public static final RegistrySupplier<Block> BARREL_MAIN = registerWithItem("barrel_main", () -> new BigBarrelMainBlock(BlockBehaviour.Properties.of().strength(2.0F, 3.0F).sound(SoundType.WOOD).noOcclusion().pushReaction(PushReaction.IGNORE)));
    public static final RegistrySupplier<Block> BARREL_MAIN_HEAD = registerWithoutItem("barrel_main_head", () -> new BigBarrelMainHeadBlock(BlockBehaviour.Properties.of().strength(2.0F, 3.0F).sound(SoundType.WOOD).noOcclusion().pushReaction(PushReaction.IGNORE).noLootTable()));
    public static final RegistrySupplier<Block> BARREL_RIGHT = registerWithoutItem("barrel_right", () -> new BigBarrelRightBlock(BlockBehaviour.Properties.of().strength(2.0F, 3.0F).sound(SoundType.WOOD).noOcclusion().pushReaction(PushReaction.IGNORE).noLootTable()));
    public static final RegistrySupplier<Block> BARREL_HEAD_RIGHT = registerWithoutItem("barrel_head_right", () -> new BigBarrelRightHeadBlock(BlockBehaviour.Properties.of().strength(2.0F, 3.0F).sound(SoundType.WOOD).noOcclusion().pushReaction(PushReaction.IGNORE).noLootTable()));
    public static final RegistrySupplier<Block> HANGING_ROPE = registerWithoutItem("hanging_rope", () -> new HangingRope(BlockBehaviour.Properties.copy(Blocks.RED_WOOL)));
    public static final RegistrySupplier<Block> BEER_MUG = registerWithItem("beer_mug", () -> new BeerMugFlowerPotBlock(BlockBehaviour.Properties.copy(Blocks.OAK_WOOD)));
    public static final RegistrySupplier<Block> BEER_WHEAT = registerWithItemeverage("beer_wheat", () -> new BeverageBlock(getMugSettings(), 2), MobEffectRegistry.SNOWWHITE);
    public static final RegistrySupplier<Block> BEER_HOPS = registerWithItemeverage("beer_hops", () -> new BeverageBlock(getMugSettings(), 2), MobEffectRegistry.PARTYSTARTER);
    public static final RegistrySupplier<Block> BEER_BARLEY = registerWithItemeverage("beer_barley", () -> new BeverageBlock(getMugSettings(), 2), MobEffectRegistry.PINTCHARISMA);
    public static final RegistrySupplier<Block> BEER_OAT = registerWithItemeverage("beer_oat", () -> new BeverageBlock(getMugSettings(), 2), MobEffectRegistry.MINING);
    public static final RegistrySupplier<Block> BEER_NETTLE = registerWithItemeverage("beer_nettle", () -> new BeverageBlock(getMugSettings(), 2), MobEffectRegistry.PACIFY);
    public static final RegistrySupplier<Block> BEER_HALEY = registerWithItemeverage("beer_haley", () -> new BeverageBlock(getMugSettings(), 2), MobEffectRegistry.HALEY);
    public static final RegistrySupplier<Block> WHISKEY_MAGGOALLAN = registerWithItemeverage("whiskey_maggoallan", () -> new BeverageBlock(getBeverageSettings(), 1), MobEffectRegistry.HEALINGTOUCH);
    public static final RegistrySupplier<Block> WHISKEY_CARRASCONLABEL = registerWithItemeverage("whiskey_carrasconlabel", () -> new BeverageBlock(getBeverageSettings(), 1), MobEffectRegistry.RENEWINGTOUCH);
    public static final RegistrySupplier<Block> WHISKEY_LILITUSINGLEMALT = registerWithItemeverage("whiskey_lilitusinglemalt", () -> new BeverageBlock(getBeverageSettings(), 1), MobEffectRegistry.PARTYSTARTER);
    public static final RegistrySupplier<Block> WHISKEY_JOJANNIK = registerWithItemeverage("whiskey_jojannik", () -> new BeverageBlock(getBeverageSettings(), 1), MobEffectRegistry.TOXICTOUCH);
    public static final RegistrySupplier<Block> WHISKEY_CRISTELWALKER = registerWithItemeverage("whiskey_cristelwalker", () -> new BeverageBlock(getBeverageSettings(), 3), MobEffectRegistry.PROTECTIVETOUCH);
    public static final RegistrySupplier<Block> WHISKEY_AK = registerWithItemeverage("whiskey_ak", () -> new BeverageBlock(getBeverageSettings(), 3), MobEffectRegistry.LIGHTNING_STRIKE);
    public static final RegistrySupplier<Block> WHISKEY_HIGHLAND_HEARTH = registerWithItemeverage("whiskey_highland_hearth", () -> new BeverageBlock(getBeverageSettings(), 1), MobEffectRegistry.REPULSION);
    public static final RegistrySupplier<Block> WHISKEY_JAMESONS_MALT = registerWithItemeverage("whiskey_jamesons_malt", () -> new BeverageBlock(getBeverageSettings(), 1), MobEffectRegistry.EXPLOSION);
    public static final RegistrySupplier<Block> WHISKEY_SMOKEY_REVERIE = registerWithItemeverage("whiskey_smokey_reverie", () -> new BeverageBlock(getBeverageSettings(), 2), MobEffectRegistry.COMBUSTION);
    public static final RegistrySupplier<Block> PORK_KNUCKLE_BLOCK = registerWithoutItem("pork_knuckle", () -> new FoodBlock(BlockBehaviour.Properties.copy(Blocks.CAKE), 4, new FoodProperties.Builder().nutrition(9).saturationMod(0.9F).build()));
    public static final RegistrySupplier<Item> PORK_KNUCKLE = registerItem("pork_knuckle", () -> new EffectBlockItem(PORK_KNUCKLE_BLOCK.get(), getFoodItemSettings(8, 0.8f, SUSTENANCE.get(), 4000)));
    public static final RegistrySupplier<Block> FRIED_CHICKEN_BLOCK = registerWithoutItem("fried_chicken", () -> new FoodBlock(BlockBehaviour.Properties.copy(Blocks.CAKE), 4, new FoodProperties.Builder().nutrition(7).saturationMod(0.7F).build()));
    public static final RegistrySupplier<Item> FRIED_CHICKEN = registerItem("fried_chicken", () -> new EffectBlockItem(FRIED_CHICKEN_BLOCK.get(), getFoodItemSettings(8, 0.8f, SUSTENANCE.get(), 4000)));
    public static final RegistrySupplier<Block> HALF_CHICKEN_BLOCK = registerWithoutItem("half_chicken", () -> new FoodBlock(BlockBehaviour.Properties.copy(Blocks.CAKE), 4, new FoodProperties.Builder().nutrition(3).saturationMod(0.4F).build()));
    public static final RegistrySupplier<Item> HALF_CHICKEN = registerItem("half_chicken", () -> new EffectBlockItem(HALF_CHICKEN_BLOCK.get(), getFoodItemSettings(8, 0.8f, SUSTENANCE.get(), 4000)));
    public static final RegistrySupplier<Block> MASHED_POTATOES_BLOCK = registerWithoutItem("mashed_potatoes", () -> new FoodBlock(BlockBehaviour.Properties.copy(Blocks.CAKE), 4, new FoodProperties.Builder().nutrition(4).saturationMod(0.5F).build()));
    public static final RegistrySupplier<Item> MASHED_POTATOES = registerItem("mashed_potatoes", () -> new EffectBlockItem(MASHED_POTATOES_BLOCK.get(), getFoodItemSettings(4, 0.6f, SATIATION.get(), 4000)));
    public static final RegistrySupplier<Block> POTATO_SALAD_BLOCK = registerWithoutItem("potato_salad", () -> new FoodBlock(BlockBehaviour.Properties.copy(Blocks.CAKE), 4, new FoodProperties.Builder().nutrition(8).saturationMod(0.8F).build()));
    public static final RegistrySupplier<Item> POTATO_SALAD = registerItem("potato_salad", () -> new EffectBlockItem(POTATO_SALAD_BLOCK.get(), getFoodItemSettings(8, 0.8f, SATIATION.get(), 6000)));
    public static final RegistrySupplier<Block> DUMPLINGS_BLOCK = registerWithoutItem("dumplings", () -> new FoodBlock(BlockBehaviour.Properties.copy(Blocks.CAKE), 4, new FoodProperties.Builder().nutrition(7).saturationMod(0.8F).build()));
    public static final RegistrySupplier<Item> DUMPLINGS = registerItem("dumplings", () -> new EffectBlockItem(DUMPLINGS_BLOCK.get(), getFoodItemSettings(7, 0.8f, SATIATION.get(), 6000)));
    public static final RegistrySupplier<Block> GINGERBREAD = registerWithItem("gingerbread", () -> new WallDecorationBlock(BlockBehaviour.Properties.copy(Blocks.FLOWER_POT)));

    public static void init() {
        Brewery.LOGGER.debug("register Mod Block and Items for " + Brewery.MOD_ID);
        ITEMS.register();
        BLOCKS.register();
    }

    public static void commonInit() {
        FuelRegistry.register(300, BEER_MUG.get(), BENCH.get(), TABLE.get(), BAR_COUNTER.get(), WOODEN_BREWINGSTATION.get());
        FuelRegistry.register(100, HOPS.get());
        FuelRegistry.register(75, PATTERNED_WOOL.get(), PATTERNED_CARPET.get());
        FuelRegistry.register(50, BREWFEST_BOOTS.get(), BREWFEST_HAT.get(), BREWFEST_DRESS.get(), BREWFEST_REGALIA.get(), BREWFEST_TROUSERS.get());
    }

    public static BlockBehaviour.Properties properties(float strength) {
        return properties(strength, strength);
    }

    public static BlockBehaviour.Properties properties(float breakSpeed, float explosionResist) {
        return BlockBehaviour.Properties.of().strength(breakSpeed, explosionResist);
    }

    private static Item.Properties getSettingsWithoutTab(Consumer<Item.Properties> consumer) {
        Item.Properties settings = new Item.Properties();
        consumer.accept(settings);
        return settings;
    }

    private static Item.Properties getSettings(Consumer<Item.Properties> consumer) {
        Item.Properties settings = new Item.Properties();
        consumer.accept(settings);
        return settings;
    }

    static Item.Properties getSettings() {
        return getSettings(settings -> {
        });
    }


    private static Item.Properties getFoodItemSettings(int nutrition, float saturationMod, MobEffect effect, int duration) {
        return getFoodItemSettings(nutrition, saturationMod, effect, duration, true, false);
    }

    private static Item.Properties getFoodItemSettings(int nutrition, float saturationMod, MobEffect effect, int duration, boolean alwaysEat, boolean fast) {
        return getSettings().food(createFood(nutrition, saturationMod, effect, duration, alwaysEat, fast));
    }

    private static BlockBehaviour.Properties getBeverageSettings() {
        return BlockBehaviour.Properties.copy(Blocks.GLASS).noOcclusion().instabreak();
    }

    private static BlockBehaviour.Properties getMugSettings() {
        return BlockBehaviour.Properties.copy(Blocks.OAK_WOOD).noOcclusion().instabreak();
    }


    private static BlockBehaviour.Properties getBushSettings() {
        return BlockBehaviour.Properties.copy(Blocks.SWEET_BERRY_BUSH);
    }

    private static FoodProperties createFood(int nutrition, float saturationMod, MobEffect effect, int duration, boolean alwaysEat, boolean fast) {
        FoodProperties.Builder food = new FoodProperties.Builder().nutrition(nutrition).saturationMod(saturationMod);
        if (alwaysEat) food.alwaysEat();
        if (fast) food.fast();
        if (effect != null) food.effect(new MobEffectInstance(effect, duration), 1.0f);
        return food.build();
    }

    private static <T extends Block> RegistrySupplier<T> registerWithItemeverage(String name, Supplier<T> block, RegistrySupplier<MobEffect> effect) {
        RegistrySupplier<T> toReturn = registerWithoutItem(name, block);
        registerItem(name, () -> new DrinkBlockItem(effect.get(), 600, toReturn.get(), getSettings(settings -> settings.food(beverageFoodComponent()))));
        return toReturn;
    }

    private static FoodProperties beverageFoodComponent() {
        FoodProperties.Builder component = new FoodProperties.Builder().nutrition(2).saturationMod(1);
        return component.build();
    }

    public static <T extends Block> RegistrySupplier<T> registerWithItem(String name, Supplier<T> block) {
        return Util.registerWithItem(BLOCKS, BLOCK_REGISTRAR, ITEMS, ITEM_REGISTRAR, new BreweryIdentifier(name), block);
    }

    public static <T extends Block> RegistrySupplier<T> registerWithoutItem(String path, Supplier<T> block) {
        return Util.registerWithoutItem(BLOCKS, BLOCK_REGISTRAR, new BreweryIdentifier(path), block);
    }

    public static <T extends Item> RegistrySupplier<T> registerItem(String path, Supplier<T> itemSupplier) {
        return Util.registerItem(ITEMS, ITEM_REGISTRAR, new BreweryIdentifier(path), itemSupplier);
    }
}