package name.modid;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

import name.modid.ui.JavaFXBootstrap;
import name.modid.ui.SettingsPanel;

public class TemplateModClient implements ClientModInitializer {

    private static KeyBinding openSettingsKey;
    private static SettingsPanel settingsPanel;

    @Override
    public void onInitializeClient() {
        // Ensure JavaFX is ready
        JavaFXBootstrap.init();

        // Register keybinding (Right Shift)
        openSettingsKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.template-mod.open_settings",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_RIGHT_SHIFT,
                "category.template-mod"
        ));

        // Render listener, runs every frame, even in menus
        WorldRenderEvents.END.register(context -> {
            while (openSettingsKey.wasPressed()) {
                JavaFXBootstrap.runLater(() -> {
                    if (settingsPanel == null) {
                        settingsPanel = new SettingsPanel();
                        settingsPanel.show();
                    } else {
                        // toggle visibility
                        if (settingsPanel.isShowing()) {
                            settingsPanel.hide();
                        } else {
                            settingsPanel.show();
                        }
                    }
                });
            }
        });
    }
}