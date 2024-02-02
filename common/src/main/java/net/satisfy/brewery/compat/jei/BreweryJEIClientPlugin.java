package net.satisfy.brewery.compat.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.IRecipeTransferRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeManager;
import net.satisfy.brewery.compat.jei.category.SiloCategory;
import net.satisfy.brewery.registry.ObjectRegistry;
import net.satisfy.brewery.util.BreweryIdentifier;


import java.util.Objects;


@JeiPlugin
public class BreweryJEIClientPlugin implements IModPlugin {

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
       }


    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        RecipeManager rm = Objects.requireNonNull(Minecraft.getInstance().level).getRecipeManager();

    }

    @Override
    public ResourceLocation getPluginUid() {
        return new BreweryIdentifier("jei_plugin");
    }

    @Override
    public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration) {

    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
       /*
        registration.addRecipeCatalyst(
                ObjectRegistry.WOODEN_BREWINGSTATION.get().asItem().getDefaultInstance(),
                BrewingStationCategory.BREWINGSTATION
        );

        registration.addRecipeCatalyst(
                ObjectRegistry.COPPER_BREWINGSTATION.get().asItem().getDefaultInstance(),
                BrewingStationCategory.BREWINGSTATION
        );

        registration.addRecipeCatalyst(
                ObjectRegistry.NETHERITE_BREWINGSTATION.get().asItem().getDefaultInstance(),
                BrewingStationCategory.BREWINGSTATION
        );*/

        registration.addRecipeCatalyst(
                ObjectRegistry.SILO_WOOD.get().asItem().getDefaultInstance(),
                SiloCategory.SILO_CATEGORY
        );

        registration.addRecipeCatalyst(
                ObjectRegistry.SILO_COPPER.get().asItem().getDefaultInstance(),
                SiloCategory.SILO_CATEGORY
        );
    }



    public static void addSlot(IRecipeLayoutBuilder builder, int x, int y, Ingredient ingredient){
        builder.addSlot(RecipeIngredientRole.INPUT, x, y).addIngredients(ingredient);
    }
}
