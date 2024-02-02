package net.satisfy.brewery.recipe;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.satisfy.brewery.registry.RecipeTypeRegistry;

public class SiloRecipe implements Recipe<Container> {
    private final ResourceLocation identifier;
    public final Ingredient input;
    private final ItemStack output;

    public SiloRecipe(ResourceLocation identifier, Ingredient input, ItemStack output) {
        this.identifier = identifier;
        this.input = input;
        this.output = output;
    }

    @Override
    public boolean matches(Container inventory, Level world) {
        return input.test(inventory.getItem(0));
    }

    @Override
    public ItemStack assemble(Container container) {
        return this.output.copy();
    }

    public ItemStack getResultItem() {
        return this.output;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> list = NonNullList.create();
        list.add(input);
        return list;
    }


    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public ResourceLocation getId() {
        return this.identifier;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return RecipeTypeRegistry.SILO_RECIPE_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return RecipeTypeRegistry.SILO_RECIPE_TYPE.get();
    }

    @Override
    public boolean isSpecial() {
        return true;
    }
    public static class Serializer implements RecipeSerializer<SiloRecipe> {

        @Override
        public SiloRecipe fromJson(ResourceLocation id, JsonObject json) {
            final Ingredient ingredient = Ingredient.fromJson(GsonHelper.getAsJsonObject(json, "input"));;

            if (ingredient.isEmpty()) {
                throw new JsonParseException("No ingredients for recipe: " + id);
            } else {
                return new SiloRecipe(id, ingredient, ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "output")));
            }
        }

        @Override
        public SiloRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
            return new SiloRecipe(id, Ingredient.fromNetwork(buf), buf.readItem());
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, SiloRecipe recipe) {
            recipe.input.toNetwork(buf);
            buf.writeItem(recipe.output);
        }
    }
}
