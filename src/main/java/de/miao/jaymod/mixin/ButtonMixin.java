package de.miao.jaymod.mixin;

import de.miao.jaymod.screen.LoginScreen;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.*;

@Mixin(ClickableWidget.class)
public abstract class ButtonMixin {

    @Shadow
    public abstract int getWidth();

    @Shadow
    protected int width;
    @Shadow
    public int x;
    @Shadow
    public int y;
    @Shadow
    protected int height;

    @Shadow
    public abstract void setAlpha(float alpha);

    @Shadow
    public abstract boolean isHovered();

    @Shadow
    public abstract void setMessage(Text message);

    @Shadow
    protected float alpha;

    @Redirect(method = "renderButton", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/widget/ClickableWidget;drawTexture(Lnet/minecraft/client/util/math/MatrixStack;IIIIII)V"))
    private void onRender(ClickableWidget instance, MatrixStack matrixStack, int i, int j, int l, int m, int n, int q) {

    }

    @Unique
    private long lastMillis = System.currentTimeMillis();

    @Unique
    private boolean hovered = isHovered();


    @Inject(at = @At(value = "HEAD"), method = "renderButton")
    private void on(MatrixStack matrices, int mouseX, int mouseY, float delta, CallbackInfo ci) {

        if (isHovered() && !hovered || !isHovered() && hovered) {
            lastMillis = System.currentTimeMillis();
            hovered = isHovered();
        }


        DrawableHelper.fill(matrices, x, y, x + width, y + height,

                new Color(191, 52, 52,
                        (isHovered() ?
                                ((int) (160 - Math.min(System.currentTimeMillis() - lastMillis, 500) / 500F * 60))
                                : (int) (Math.min(System.currentTimeMillis() - lastMillis, 500) / 500F * 60 + 100))).getRGB());
    }
}

@Mixin(TitleScreen.class)
class ButtonMixin2 extends Screen {

    protected ButtonMixin2(Text title) {
        super(title);
    }

    @Inject(at = @At(value = "HEAD", ordinal = 0), method = "initWidgetsNormal")
    private void modify(int y, int spacingY, CallbackInfo ci) {

        this.addDrawableChild(new ButtonWidget(this.width / 2 - 100, (int)(y - spacingY) , 200, 20, Text.of("Cracked Login"), button -> this.client.setScreen(new LoginScreen(this, this::addEntry))));

    }

    private void addEntry(boolean confirmedAction) {
        this.client.setScreen(this);
    }


}

