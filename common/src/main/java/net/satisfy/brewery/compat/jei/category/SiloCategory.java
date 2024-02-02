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
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.satisfy.brewery.Brewery;
import net.satisfy.brewery.compat.jei.BreweryJEIClientPlugin;
import net.satisfy.brewery.recipe.SiloRecipe;
import net.satisfy.brewery.registry.ObjectRegistry;

public class SiloCategory implements IRecipeCategory<SiloRecipe> {
    public static final RecipeType<SiloRecipe> SILO_CATEGORY = RecipeType.create(Brewery.MOD_ID, "apple_mashing", SiloRecipe.class);
    private static final int SLOT_SIZE = 22;
    private static final int WIDTH_OF = 26;
    private static final int HEIGHT_OF = 13;
    private final IDrawable icon;
    private final IDrawable background;
    private final Component localizedName;
    private final IDrawable slotIcon;

    public SiloCategory(IGuiHelper helper) {
        ResourceLocation backgroundImage = new ResourceLocation(Brewery.MOD_ID, "textures/gui/jei/silo.png");
        background = helper.createDrawable(backgroundImage, 0, 0, 118, 80);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, ObjectRegistry.SILO_WOOD.get().asItem().getDefaultInstance());
        this.localizedName = Component.translatable("rei.brewery.silo_category");
        slotIcon = helper.createDrawable(backgroundImage, 119, 0, SLOT_SIZE, SLOT_SIZE);
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, SiloRecipe recipe, IFocusGroup focuses) {
        int inputSlotX = 48 - WIDTH_OF;
        int inputSlotY = 34 - HEIGHT_OF;
        int outputSlotX = 116 - WIDTH_OF;
        int outputSlotY = 35 - HEIGHT_OF;

        BreweryJEIClientPlugin.addSlot(builder, inputSlotX, inputSlotY, recipe.input);
        builder.addSlot(RecipeIngredientRole.OUTPUT, outputSlotX, outputSlotY).addItemStack(recipe.getResultItem());
    }

    @Override
    public void draw(SiloRecipe recipe, IRecipeSlotsView recipeSlotsView, PoseStack ms, double mouseX, double mouseY) {
        this.slotIcon.draw(ms, 63, 53);
    }

    @Override
    public RecipeType<SiloRecipe> getRecipeType() {
        return SILO_CATEGORY;
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
