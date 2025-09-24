package name.modid;

import name.modid.ui.SettingsWindow;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;
import javafx.application.Platform;

public class TemplateModClient implements ClientModInitializer {
    private static final KeyBinding openSettings =
        KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.template-mod.opensettings",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_RIGHT_SHIFT,
            "category.template-mod"
        ));

    @Override
    public void onInitializeClient() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (openSettings.wasPressed()) {
                Platform.runLater(() -> {
                    SettingsWindow settingsWindow = new SettingsWindow();
                    settingsWindow.show();
                });
            }
        });
    }
}