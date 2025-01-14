package net.satisfy.brewery.core.registry;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ArmorMaterials;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.NotNull;

public class ArmorMaterialRegistry {
    public static final ArmorMaterial BREWFEST = new ArmorMaterial() {

        @Override
        public int getDurabilityForType(ArmorItem.Type type) {
            return (int) (ArmorMaterials.LEATHER.getDurabilityForType(type) * 0.8); 
        }

        @Override
        public int getDefenseForType(ArmorItem.Type type) {
            return Math.max(0, ArmorMaterials.LEATHER.getDefenseForType(type) - 1); 
        }

        @Override
        public int getEnchantmentValue() {
            return Math.max(0, ArmorMaterials.LEATHER.getEnchantmentValue() - 1); 
        }

        @Override
        public @NotNull SoundEvent getEquipSound() {
            return SoundEvents.ARMOR_EQUIP_LEATHER;
        }

        @Override
        public @NotNull Ingredient getRepairIngredient() {
            return Ingredient.of(Items.RABBIT_HIDE);
        }

        @Override
        public @NotNull String getName() {
            return "brewfest";
        }

        @Override
        public float getToughness() {
            return Math.max(0, ArmorMaterials.LEATHER.getToughness() - 0.5f); 
        }

        @Override
        public float getKnockbackResistance() {
            return Math.max(0, ArmorMaterials.LEATHER.getKnockbackResistance() - 0.05f); 
        }
    };
}
