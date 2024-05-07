package net.satisfy.brewery.mixin;

import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import satisfy.farm_and_charm.block.SiloBlock;
import satisfy.farm_and_charm.registry.ObjectRegistry;

@Mixin(SiloBlock.class)
public class SiloBlockMixin {

    @Inject(method = "initializeDryersIfNeeded()V", at = @At("RETURN"), remap = false)
    private static void onInitializeDryersIfNeeded(CallbackInfo ci) {
        SiloBlock.addDry(ObjectRegistry.BARLEY.get(), net.satisfy.brewery.registry.ObjectRegistry.DRIED_BARLEY.get());
        SiloBlock.addDry(ObjectRegistry.OAT.get(), net.satisfy.brewery.registry.ObjectRegistry.DRIED_OAT.get());
        SiloBlock.addDry(ObjectRegistry.CORN.get(), net.satisfy.brewery.registry.ObjectRegistry.DRIED_CORN.get());
        SiloBlock.addDry(Items.WHEAT, net.satisfy.brewery.registry.ObjectRegistry.DRIED_WHEAT.get());
    }
}
