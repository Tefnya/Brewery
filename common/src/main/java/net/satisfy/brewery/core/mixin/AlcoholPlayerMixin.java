package net.satisfy.brewery.core.mixin;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import net.satisfy.brewery.core.effect.alcohol.AlcoholLevel;
import net.satisfy.brewery.core.effect.alcohol.AlcoholManager;
import net.satisfy.brewery.core.effect.alcohol.AlcoholPlayer;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class AlcoholPlayerMixin implements AlcoholPlayer {
    @Shadow
    @Final
    protected RandomSource random;
    @Shadow
    private Vec3 deltaMovement;
    @Unique
    @NotNull
    private AlcoholLevel brewery$alcoholLevel = new AlcoholLevel();

    @Override
    public AlcoholLevel brewery$getAlcohol() {
        return this.brewery$alcoholLevel;
    }

    @Override
    public void brewery$setAlcohol(AlcoholLevel alcoholLevel) {
        this.brewery$alcoholLevel = alcoholLevel;
    }

    @Inject(method = "saveWithoutId", at = @At("HEAD"))
    protected void injectWriteMethod(CompoundTag nbt, CallbackInfoReturnable<CompoundTag> cir) {
        nbt.putInt("brewery.drunkenness", this.brewery$alcoholLevel.getDrunkenness());
        nbt.putInt("brewery.immunity", this.brewery$alcoholLevel.getImmunity());
    }

    @Inject(method = "load", at = @At(value = "HEAD"))
    protected void injectReadMethod(CompoundTag nbt, CallbackInfo ci) {
        int drunkenness = nbt.contains("brewery.drunkenness") ? nbt.getInt("brewery.drunkenness") : 0;
        int immunity = nbt.contains("brewery.immunity") ? nbt.getInt("brewery.immunity") : 3;
        this.brewery$alcoholLevel = new AlcoholLevel(drunkenness, immunity);
    }

    @Inject(method = "getDeltaMovement", at = @At(value = "HEAD"), cancellable = true)
    public void alcoholMovement(CallbackInfoReturnable<Vec3> cir) {
        if (this.brewery$getAlcohol().isDrunk() && this.random.nextFloat() < 0.05f) {
            cir.setReturnValue(AlcoholManager.stagger(deltaMovement, this.random));
        }
    }
}
