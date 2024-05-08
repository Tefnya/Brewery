package net.satisfy.brewery.registry;

import de.cristelknight.doapi.common.util.datafixer.DataFixers;
import de.cristelknight.doapi.common.util.datafixer.StringPairs;
import net.satisfy.brewery.Brewery;

public class DataFixerRegistry {

    public static void init() {
        StringPairs p = DataFixers.getOrCreate(Brewery.MOD_ID);
        p.add("brewery:barley_crop", "farm_and_charm:barley_crop");
        p.add("brewery:barley_seeds", "farm_and_charm:barley_seeds");
        p.add("brewery:barley", "farm_and_charm:barley");
        p.add("brewery:corn_crop", "farm_and_charm:corn_crop");
        p.add("brewery:corn_seeds", "farm_and_charm:kernels");
        p.add("brewery:corn", "farm_and_charm:corn");
        p.add("brewery:silo_wood", "farm_and_charm:silo_wood");
        p.add("brewery:silo_copper", "farm_and_charm:silo_copper");
    }
}
