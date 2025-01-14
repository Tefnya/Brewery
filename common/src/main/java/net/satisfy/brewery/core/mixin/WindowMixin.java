package net.satisfy.brewery.core.mixin;

import com.mojang.blaze3d.platform.Window;
import net.satisfy.brewery.core.effect.alcohol.MotionBlur;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Window.class)
public class WindowMixin {

    @Inject(method = "onFramebufferResize", at = @At("TAIL"))
    private void updateShaderSize(CallbackInfo ci) {
        if (MotionBlur.enabled)
            MotionBlur.shader.resize(MotionBlur.client.getWindow().getWidth(), MotionBlur.client.getWindow().getHeight());
    }
}