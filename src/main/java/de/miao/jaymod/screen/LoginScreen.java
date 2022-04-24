package de.miao.jaymod.screen;

import de.miao.jaymod.util.SessionUtil;
import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.AddServerScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ScreenTexts;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.network.ServerAddress;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

public class LoginScreen extends Screen {
    private static final Text ENTER_NAME_TEXT = Text.of("Account Name");
    private final BooleanConsumer callback;

    private ButtonWidget addButton;
    private TextFieldWidget nameField;
    private final Screen parent;

    public LoginScreen(Screen parent, BooleanConsumer callback) {
        super(Text.of("Cracked Login"));
        this.parent = parent;
        this.callback = callback;
     }

    @Override
    public void tick() {
        this.nameField.tick();
    }

    private void addAndClose() {
        SessionUtil.changeCrackedName(nameField.getText().replaceAll("&", "§"));
        SessionUtil.defaultSession = false;
        this.callback.accept(true);

    }

    @Override
    protected void init() {
        this.client.keyboard.setRepeatEvents(true);
        this.nameField = new TextFieldWidget(this.textRenderer, this.width / 2 - 100, 106, 200, 20, new TranslatableText("addServer.enterIp"));
        this.nameField.setMaxLength(128);
        this.nameField.setChangedListener(address -> this.updateAddButton());
        this.addSelectableChild(this.nameField);
        this.addButton = this.addDrawableChild(new ButtonWidget(this.width / 2 - 100, this.height / 4 + 96 + 18, 200, 20, new TranslatableText("addServer.add"), button ->
                addAndClose()

        ));
        this.addDrawableChild(new ButtonWidget(this.width / 2 - 100, this.height / 4 + 120 + 18, 200, 20, ScreenTexts.CANCEL, button -> this.callback.accept(false)));
        this.addDrawableChild(new ButtonWidget(this.width / 2 - 100, this.height / 4 + 144 + 18, 200, 20, Text.of("Reset"), button -> {
            SessionUtil.defaultSession = true;
            this.callback.accept(false);
        }));
        this.updateAddButton();
    }

    @Override
    public void resize(MinecraftClient client, int width, int height) {
        String string = this.nameField.getText();
        this.init(client, width, height);
        this.nameField.setText(string);
    }

    @Override
    public void removed() {
        this.client.keyboard.setRepeatEvents(false);
    }


    @Override
    public void close() {
        this.client.setScreen(this.parent);
    }

    private void updateAddButton() {
        this.addButton.active = ServerAddress.isValid(this.nameField.getText()) && !this.nameField.getText().isEmpty();
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);
        AddServerScreen.drawCenteredText(matrices, this.textRenderer, this.title, this.width / 2, 17, 0xFFFFFF);
        AddServerScreen.drawTextWithShadow(matrices, this.textRenderer, ENTER_NAME_TEXT, this.width / 2 - 100, 94, 0xA0A0A0);
        this.nameField.render(matrices, mouseX, mouseY, delta);
        super.render(matrices, mouseX, mouseY, delta);
    }

}
