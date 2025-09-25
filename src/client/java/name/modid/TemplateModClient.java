package name.modid;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

import name.modid.ui.JavaFXBootstrap;
import name.modid.ui.SettingsWindow;

public class TemplateModClient implements ClientModInitializer {

    private static KeyBinding openSettingsKey;
    private static SettingsWindow settingsWindow;

    @Override
    public void onInitializeClient() {
        // Ensure JavaFX is ready
        JavaFXBootstrap.init();

        // FOR DEBUGGING: Open window on launch to verify UI
        JavaFXBootstrap.runLater(() -> {
            if (settingsWindow == null) {
                settingsWindow = new SettingsWindow();
                settingsWindow.show();
            }
        });

        // The keybinding logic is temporarily disabled below to allow for testing.
        /*
        // Register keybinding (Right Shift)
        openSettingsKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.template-mod.open_settings",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_RIGHT_SHIFT,
                "category.template-mod"
        ));

        // Tick listener
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (openSettingsKey.wasPressed()) {
                JavaFXBootstrap.runLater(() -> {
                    if (settingsWindow == null) {
                        settingsWindow = new SettingsWindow();
                        settingsWindow.show();
                    } else {
                        // toggle visibility
                        if (settingsWindow.isShowing()) {
                            settingsWindow.hide();
                        } else {
                            settingsWindow.show();
                        }
                    }
                });
            }
        });
        */
    }
}