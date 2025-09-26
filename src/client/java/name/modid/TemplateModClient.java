package name.modid;

import javafx.application.Platform;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;
import name.modid.ui.SettingsWindow;

public class TemplateModClient implements ClientModInitializer {

    private static KeyBinding openSettings;

    @Override
    public void onInitializeClient() {
        openSettings = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.template-mod.open_settings", // Using the existing key name
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_RIGHT_SHIFT,
                "category.templatemod"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (openSettings.wasPressed()) {
                Platform.runLater(() -> {
                    if (SettingsWindow.getStage().isShowing()) {
                        SettingsWindow.getStage().hide();
                    } else {
                        SettingsWindow.showWindow();
                    }
                });
            }
        });
    }
}