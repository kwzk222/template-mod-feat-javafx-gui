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

    private static KeyBinding openSettingsKey;
    private static SettingsWindow settingsWindow;

    @Override
    public void onInitializeClient() {
        // Register keybinding: Right Shift
        openSettingsKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.template-mod.open_settings",       // translation key
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_RIGHT_SHIFT,              // the actual key
                "category.template-mod"                 // category in controls menu
        ));

        // Listen for key press
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (openSettingsKey.wasPressed()) {
                // Run on JavaFX UI thread
                Platform.runLater(() -> {
                    if (settingsWindow == null) {
                        settingsWindow = new SettingsWindow();
                        settingsWindow.show();
                    } else {
                        settingsWindow.show(); // reopen if hidden
                    }
                });
            }
        });
    }
}