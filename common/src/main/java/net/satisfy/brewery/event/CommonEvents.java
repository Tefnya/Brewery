package net.satisfy.brewery.event;

import dev.architectury.event.events.common.LootEvent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootDataManager;
import net.satisfy.brewery.util.BreweryLoottableInjector;

public class CommonEvents {

    public static void init() {
        LootEvent.MODIFY_LOOT_TABLE.register(CommonEvents::onModifyLootTable);
    }

    public static void onModifyLootTable(LootDataManager tables, ResourceLocation id, LootEvent.LootTableModificationContext context, boolean builtin) {
        BreweryLoottableInjector.InjectLoot(id, context);
    }
}
