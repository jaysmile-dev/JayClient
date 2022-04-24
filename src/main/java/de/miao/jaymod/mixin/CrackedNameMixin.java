package de.miao.jaymod.mixin;

import de.miao.jaymod.util.SessionUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.Session;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MinecraftClient.class)
public class CrackedNameMixin {


    @Inject(at = {@At("HEAD")},
            method = {"getSession()Lnet/minecraft/client/util/Session;"},
            cancellable = true)
    private void onGetSession(CallbackInfoReturnable<Session> cir) {
        if (SessionUtil.getNewSession() == null)
            return;
        if (!SessionUtil.defaultSession)
            cir.setReturnValue(SessionUtil.getNewSession());
    }


}
