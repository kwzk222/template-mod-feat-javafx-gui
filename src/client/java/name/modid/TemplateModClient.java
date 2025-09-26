package name.modid;

import javafx.application.Platform;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;
import name.modid.ui.JavaFXInitializer;
import name.modid.ui.SettingsWindow;

public class TemplateModClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        // Step 3.1: Initialize JavaFX toolkit once
        JavaFXInitializer.ensureInitialized();

        // Step 3.2: Register keybinding
        KeyBinding openSettings = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.template_mod.open_settings",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_RIGHT_SHIFT,
            "category.template_mod"
        ));

        // Step 3.3: Open/close the window
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (openSettings.wasPressed()) {
                Platform.runLater(() -> {
                    if (!SettingsWindow.isShowing()) {
                        SettingsWindow.showWindow();
                    } else {
                        SettingsWindow.hideWindow();
                    }
                });
            }
        });
    }
}