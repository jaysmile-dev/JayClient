package de.miao.jaymod.mixin;

import de.miao.jaymod.screen.LoginScreen;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClickableWidget.class)
public abstract class ButtonMixin {

    private @Unique
    float alpha = 1;

    private static final @Unique
    Identifier TRANSPARENT = new Identifier("jaymod", "textures/transparent_button.png");

    private static final @Unique
    Identifier TRANSPARENT_HOVER = new Identifier("jaymod", "textures/transparent_button_hover.png");

    @Redirect(method = "renderButton", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/widget/ClickableWidget;drawTexture(Lnet/minecraft/client/util/math/MatrixStack;IIIIII)V"))
    private void onRender(ClickableWidget instance, MatrixStack matrixStack, int i, int j, int l, int m, int n, int q) {
        DrawableHelper.fill(matrixStack, instance.x, instance.y, instance.x + instance.getWidth(), instance.y + instance.getHeight(), 0x8f3333);
        instance.setAlpha(alpha);


        if (instance.isHovered() && alpha < 1)
            alpha += 0.001F;
        else if (!instance.isHovered() && alpha > 0)
            alpha -= 0.001F;


    }

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/widget/ClickableWidget;isHovered()Z"), method = "renderButton")
    private void on(MatrixStack matrices, int mouseX, int mouseY, float delta, CallbackInfo ci) {

    }


}

@Mixin(TitleScreen.class)
class ButtonMixin2 extends Screen {

    protected ButtonMixin2(Text title) {
        super(title);
    }

    int l = this.height / 4 + 89;

    @Inject(at = @At("TAIL"), method = "initWidgetsNormal")
    private void modify(CallbackInfo ci) {

        this.addDrawableChild(new ButtonWidget(this.width / 2 - 100, l, 200, 20, Text.of("Cracked Login"), button -> this.client.setScreen(new LoginScreen(this, this::addEntry))));

    }

    private void addEntry(boolean confirmedAction) {
        this.client.setScreen(this);
    }


}

