package name.modid;

import name.modid.ui.SettingsWindow;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import org.lwjgl.glfw.GLFW;

public class TemplateModClient implements ClientModInitializer {
    private static SettingsWindow settingsWindow;
    private static KeyBinding toggleGuiKeyBinding;

    @Override
    public void onInitializeClient() {
        toggleGuiKeyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.template-mod.toggle_gui",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_RIGHT_SHIFT,
                "category.template-mod.main"
        ));

        new JFXPanel(); // Initializes the JavaFX toolkit

        Platform.runLater(() -> {
            settingsWindow = new SettingsWindow();
        });

        ClientTickEvents.START_CLIENT_TICK.register(client -> {
            if (client == null) return;

            while (toggleGuiKeyBinding.wasPressed()) {
                if (settingsWindow != null) {
                    Platform.runLater(() -> {
                        if (settingsWindow.isShowing()) {
                            settingsWindow.hide();
                        } else {
                            settingsWindow.show();
                        }
                    });
                }
            }
        });
    }
}