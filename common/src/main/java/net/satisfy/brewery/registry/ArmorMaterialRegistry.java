package net.satisfy.brewery.registry;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ArmorMaterials;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.NotNull;

public class ArmorMaterialRegistry {
    private static final Ingredient WOOL_REPAIR_INGREDIENT = Ingredient.of(ItemTags.WOOL);

    public static final ArmorMaterial BREWFEST_ARMOR = new BrewfestArmorMaterial("brewfest", ArmorMaterials.LEATHER, WOOL_REPAIR_INGREDIENT);
    public static final ArmorMaterial BREWFEST_LEATHER = BREWFEST_ARMOR;
    public static final ArmorMaterial BREWFEST_DRESS = new BrewfestArmorMaterial("dirndl", ArmorMaterials.LEATHER, WOOL_REPAIR_INGREDIENT, ArmorMaterials.IRON.getEnchantmentValue(), ArmorMaterials.TURTLE.getEquipSound());

    private static class BrewfestArmorMaterial implements ArmorMaterial {
        private final String name;
        private final ArmorMaterial delegate;
        private final Ingredient repairIngredient;
        private final int enchantmentValue;
        private final SoundEvent equipSound;

        BrewfestArmorMaterial(String name, ArmorMaterial delegate, Ingredient repairIngredient) {
            this(name, delegate, repairIngredient, delegate.getEnchantmentValue(), delegate.getEquipSound());
        }

        BrewfestArmorMaterial(String name, ArmorMaterial delegate, Ingredient repairIngredient, int enchantmentValue, SoundEvent equipSound) {
            this.name = name;
            this.delegate = delegate;
            this.repairIngredient = repairIngredient;
            this.enchantmentValue = enchantmentValue;
            this.equipSound = equipSound;
        }

        public int getDurabilityForType(ArmorItem.Type type) {
            return delegate.getDurabilityForType(type);
        }

        public int getDefenseForType(ArmorItem.Type type) {
            return 1;
        }

        public int getEnchantmentValue() {
            return enchantmentValue;
        }

        public @NotNull SoundEvent getEquipSound() {
            return equipSound;
        }

        public @NotNull Ingredient getRepairIngredient() {
            return repairIngredient;
        }

        public @NotNull String getName() {
            return name;
        }

        public float getToughness() {
            return delegate.getToughness();
        }

        public float getKnockbackResistance() {
            return delegate.getKnockbackResistance();
        }
    }
}
