package de.miao.jaymod.mixin;

import net.minecraft.server.network.ServerLoginNetworkHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerLoginNetworkHandler.class)
public class LoginMixin {

    @Inject(method = "isValidName", at = @At(value = "HEAD"), cancellable = true)
    private static void on(String name, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(true);
    }
}
