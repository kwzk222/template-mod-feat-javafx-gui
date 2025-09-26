package name.modid;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;
import name.modid.ui.SettingsWindow;

public class TemplateModClient implements ClientModInitializer {

    private static boolean fxInitialized = false;

    @Override
    public void onInitializeClient() {
        // Initialize JavaFX toolkit once
        if (!fxInitialized) {
            new JFXPanel(); // This initializes JavaFX runtime
            fxInitialized = true;
        }

        // Register keybinding to open settings
        KeyBinding openSettings = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.template_mod.open_settings",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_RIGHT_SHIFT,
            "category.template_mod"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (openSettings.wasPressed()) {
                Platform.runLater(() -> {
                    if (!SettingsWindow.isOpen()) {
                        SettingsWindow.showWindow();
                    }
                });
            }
        });
    }
}