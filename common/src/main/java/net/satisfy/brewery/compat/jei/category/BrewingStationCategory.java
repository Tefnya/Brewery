package net.satisfy.brewery.compat.jei.category;

import com.mojang.blaze3d.vertex.PoseStack;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import net.satisfy.brewery.Brewery;
import net.satisfy.brewery.compat.jei.BreweryJEIClientPlugin;
import net.satisfy.brewery.recipe.BrewingRecipe;
import net.satisfy.brewery.registry.ObjectRegistry;

public class BrewingStationCategory implements IRecipeCategory<BrewingRecipe> {
    public static final RecipeType<BrewingRecipe> BREWINGSTATION = RecipeType.create(Brewery.MOD_ID, "brewing", BrewingRecipe.class);
    private static final int SLOT_SIZE = 22;
    private static final int WIDTH_OF = 26;
    private static final int HEIGHT_OF = 13;
    private final IDrawable icon;
    private final IDrawable background;
    private final Component localizedName;
    private final IDrawable slotIcon;

    public BrewingStationCategory(IGuiHelper helper) {
        ResourceLocation backgroundImage = new ResourceLocation(Brewery.MOD_ID, "textures/gui/jei/silo.png");
        background = helper.createDrawable(backgroundImage, 0, 0, 118, 80);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, ObjectRegistry.SILO_WOOD.get().asItem().getDefaultInstance());
        this.localizedName = Component.translatable("rei.brewery.brewing_station_category");
        slotIcon = helper.createDrawable(backgroundImage, 119, 0, SLOT_SIZE, SLOT_SIZE);
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, BrewingRecipe recipe, IFocusGroup focuses) {
        NonNullList<Ingredient> ingredients = recipe.getIngredients();
        int s = ingredients.size();
        int outputSlotX = 116 - WIDTH_OF;
        int outputSlotY = 35 - HEIGHT_OF;

        if(s > 0) BreweryJEIClientPlugin.addSlot(builder, 33 - WIDTH_OF, 26 - HEIGHT_OF, ingredients.get(0));
        if(s > 1) BreweryJEIClientPlugin.addSlot(builder, 51 - WIDTH_OF, 26 - HEIGHT_OF, ingredients.get(1));
        if(s > 2) BreweryJEIClientPlugin.addSlot(builder, 33 - WIDTH_OF, 44 - HEIGHT_OF, ingredients.get(2));
        if(s > 3) BreweryJEIClientPlugin.addSlot(builder, 51 - WIDTH_OF, 44 - HEIGHT_OF, ingredients.get(3));

        builder.addSlot(RecipeIngredientRole.OUTPUT, outputSlotX, outputSlotY).addItemStack(recipe.getResultItem());
    }
    
    @Override
    public void draw(BrewingRecipe recipe, IRecipeSlotsView recipeSlotsView, PoseStack ms, double mouseX, double mouseY) {
        this.slotIcon.draw(ms, 63, 53);
    }
    
    @Override
    public RecipeType<BrewingRecipe> getRecipeType() {
        return BREWINGSTATION;
    }

    @Override
    public Component getTitle() {
        return this.localizedName;
    }

    @Override
    public IDrawable getBackground() {
        return this.background;
    }

    @Override
    public IDrawable getIcon() {
        return this.icon;
    }
}
