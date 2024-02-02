package net.satisfy.brewery.registry;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.Registry;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.satisfy.brewery.Brewery;
import net.satisfy.brewery.recipe.BrewingRecipe;
import net.satisfy.brewery.recipe.SiloRecipe;

import java.util.function.Supplier;

public class RecipeTypeRegistry {
    private static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(Brewery.MOD_ID, Registry.RECIPE_SERIALIZER_REGISTRY);
    private static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(Brewery.MOD_ID, Registry.RECIPE_TYPE_REGISTRY);

    public static final RegistrySupplier<RecipeType<BrewingRecipe>> BREWING_RECIPE_TYPE = create("brewing");
    public static final RegistrySupplier<RecipeType<SiloRecipe>> SILO_RECIPE_TYPE = create("drying");

    public static final RegistrySupplier<RecipeSerializer<BrewingRecipe>> BREWING_RECIPE_SERIALIZER = create("brewing", BrewingRecipe.Serializer::new);
    public static final RegistrySupplier<RecipeSerializer<SiloRecipe>> SILO_RECIPE_SERIALIZER = create("drying", SiloRecipe.Serializer::new);

    private static <T extends Recipe<?>> RegistrySupplier<RecipeSerializer<T>> create(String name, Supplier<RecipeSerializer<T>> serializer) {
        return RECIPE_SERIALIZERS.register(name, serializer);
    }

    private static <T extends Recipe<?>> RegistrySupplier<RecipeType<T>> create(String name) {
        Supplier<RecipeType<T>> type = () -> new RecipeType<>() {
            @Override
            public String toString() {
                return name;
            }
        };
        return RECIPE_TYPES.register(name, type);
    }

    public static void init() {
        RECIPE_SERIALIZERS.register();
        RECIPE_TYPES.register();
    }
}
