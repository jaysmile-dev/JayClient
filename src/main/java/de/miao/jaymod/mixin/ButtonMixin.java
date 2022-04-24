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
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClickableWidget.class)
public abstract class ButtonMixin {

    @Shadow
    public abstract int getWidth();

    @Shadow protected int width;
    @Shadow public int x;
    @Shadow public int y;
    @Shadow protected int height;
    private @Unique
    float alpha = 1;

    private static final @Unique
    Identifier TRANSPARENT = new Identifier("jaymod", "textures/transparent_button.png");

    private static final @Unique
    Identifier TRANSPARENT_HOVER = new Identifier("jaymod", "textures/transparent_button_hover.png");

    @Redirect(method = "renderButton", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/widget/ClickableWidget;drawTexture(Lnet/minecraft/client/util/math/MatrixStack;IIIIII)V"))
    private void onRender(ClickableWidget instance, MatrixStack matrixStack, int i, int j, int l, int m, int n, int q) {

    }


    @Inject(at = @At(value = "HEAD"), method = "renderButton")
    private void on(MatrixStack matrices, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        DrawableHelper.fill(new MatrixStack(), x, y, x + width, y + height, 0xff00ce);

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

