package net.satisfy.brewery.compat.rei.display;

import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import net.satisfy.brewery.compat.rei.category.SiloCategory;

import java.util.List;

public class SiloDisplay extends BasicDisplay {

    public SiloDisplay(List<EntryIngredient> inputs, List<EntryIngredient> outputs) {
        super(inputs, outputs);
    }

    @Override
    public CategoryIdentifier<?> getCategoryIdentifier() {
        return SiloCategory.SILO_DISPLAY;
    }
}