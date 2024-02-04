package net.satisfy.brewery.compat.jei.category;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.satisfy.brewery.Brewery;
import net.satisfy.brewery.compat.jei.BreweryJEIClientPlugin;
import net.satisfy.brewery.recipe.SiloRecipe;
import net.satisfy.brewery.registry.ObjectRegistry;

public class SiloCategory implements IRecipeCategory<SiloRecipe> {
    public final static ResourceLocation UID = new ResourceLocation(Brewery.MOD_ID, "drying");
    public final static ResourceLocation TEXTURE =
            new ResourceLocation(Brewery.MOD_ID, "textures/gui/silo.png");

    private final IDrawable background;
    private final IDrawable icon;

    public SiloCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(TEXTURE, 0, 0, 176, 85);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ObjectRegistry.SILO_WOOD.get()));
    }

    @Override
    public RecipeType<SiloRecipe> getRecipeType() {
        return BreweryJEIClientPlugin.DRYING_TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("rei.brewery.silo_category");
    }

    @Override
    public IDrawable getBackground() {
        return this.background;
    }

    @Override
    public IDrawable getIcon() {
        return this.icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, SiloRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 50, 35).addIngredients(recipe.getIngredients().get(0));

        builder.addSlot(RecipeIngredientRole.OUTPUT, 110, 35).addItemStack(recipe.getResultItem(null));
    }
}