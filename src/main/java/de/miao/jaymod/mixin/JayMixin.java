package de.miao.jaymod.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.boss.BossBar;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.Matrix4f;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

import java.awt.*;

@Mixin(TitleScreen.class)
public abstract class JayMixin {

    private static final @Unique
    Identifier BACKGROUND = new Identifier("jaymod", "textures/background.png");
    private static final @Unique
    Identifier TRANSPARENT = new Identifier("jaymod", "textures/transparent.png");

    @ModifyArg(at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;setShaderTexture(ILnet/minecraft/util/Identifier;)V", ordinal = 0), method = "render")
    private Identifier injectBackground(Identifier identifier) {
        return BACKGROUND;
    }

    @ModifyArg(at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;setShaderTexture(ILnet/minecraft/util/Identifier;)V", ordinal = 1), method = "render")
    private Identifier injectTitle(Identifier identifier) {
        return TRANSPARENT;
    }

    @ModifyArg(at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;setShaderTexture(ILnet/minecraft/util/Identifier;)V", ordinal = 2), method = "render")
    private Identifier injectSubTitle(Identifier identifier) {

        return TRANSPARENT;
    }

    @ModifyArgs(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/TitleScreen;drawCenteredText(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/font/TextRenderer;Ljava/lang/String;III)V"), method = "render")
    private void modify(Args args) {
        args.set(2, "雅各布");
        args.set(5, 0xffffff);
    }


}

@Mixin(InGameHud.class)
class JayMixin2 {

    private static Identifier TITLE = new Identifier("jaymod", "textures/title.png");
    private static String version = "v1.0";
    @Inject(
            at = {@At(value = "INVOKE",
                    target = "Lcom/mojang/blaze3d/systems/RenderSystem;enableBlend()V",
                    ordinal = 4)},
            method = {"render(Lnet/minecraft/client/util/math/MatrixStack;F)V"})
    private void onRender(MatrixStack matrixStack, float partialTicks,
                          CallbackInfo ci) {

        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        var color = Color.WHITE;
        float red = color.getRed() / 255F;
        float green = color.getGreen() / 255F;
        float blue = color.getBlue() / 255F;

        drawQuads(matrixStack, 0, 6, MinecraftClient.getInstance().textRenderer.getWidth(version) + 76, 17, 228 / 255F,
                77 / 255F, 77 / 255F, 0.5F);

        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        MinecraftClient.getInstance().textRenderer.draw(matrixStack, version, 74, 8, 0xffffff);

        RenderSystem.setShaderColor(1, 1, 1, 1);
        GL11.glEnable(GL11.GL_BLEND);
        RenderSystem.setShaderTexture(0, TITLE);
        DrawableHelper.drawTexture(matrixStack, 0, 3, 0, 0, 72, 18, 72, 18);
    }

    private void drawQuads(MatrixStack matrices, int x1, int y1, int x2, int y2,
                           float r, float g, float b, float a)
    {
        Matrix4f matrix = matrices.peek().getPositionMatrix();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);

        BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
        bufferBuilder.begin(VertexFormat.DrawMode.QUADS,
                VertexFormats.POSITION_COLOR);
        bufferBuilder.vertex(matrix, x1, y2, 0.0F).color(r, g, b, a).next();
        bufferBuilder.vertex(matrix, x2, y2, 0.0F).color(r, g, b, a).next();
        bufferBuilder.vertex(matrix, x2, y1, 0.0F).color(r, g, b, a).next();
        bufferBuilder.vertex(matrix, x1, y1, 0.0F).color(r, g, b, a).next();
        bufferBuilder.end();
        BufferRenderer.draw(bufferBuilder);
    }
}

