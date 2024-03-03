package net.satisfy.brewery.client;

import de.cristelknight.doapi.client.render.block.storage.StorageBlockEntityRenderer;
import de.cristelknight.doapi.client.render.block.storage.StorageTypeRenderer;
import net.minecraft.resources.ResourceLocation;
import net.satisfy.brewery.client.render.BeverageRenderer;
import net.satisfy.brewery.registry.StorageTypeRegistry;

public class ClientStorageTypes {

    public static void registerStorageType(ResourceLocation location, StorageTypeRenderer renderer){
        StorageBlockEntityRenderer.registerStorageType(location, renderer);
    }

    public static void init(){
        registerStorageType(StorageTypeRegistry.WINE_BOTTLE, new BeverageRenderer());
    }

}
