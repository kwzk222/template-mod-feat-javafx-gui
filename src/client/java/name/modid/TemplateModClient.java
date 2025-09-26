package name.modid;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;
import javafx.application.Platform;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import name.modid.ui.SettingsPanel;

public class TemplateModClient implements ClientModInitializer {

    private Stage settingsStage;

    @Override
    public void onInitializeClient() {
        JavaFXInitializer.init(); // ensure FX toolkit is started

        KeyBinding openSettings = KeyBindingHelper.registerKeyBinding(
            new KeyBinding("key.template_mod.settings", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_RIGHT_SHIFT, "key.categories.template_mod")
        );

        net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (openSettings.wasPressed()) {
                Platform.runLater(this::showSettingsWindow);
            }
        });
    }

    private void showSettingsWindow() {
        if (settingsStage == null) {
            settingsStage = new Stage();
            SettingsPanel panel = new SettingsPanel();

            ScrollPane scrollPane = new ScrollPane(panel);
            scrollPane.setFitToWidth(true);
            scrollPane.setStyle("-fx-background: #1c1c1c; -fx-border-color: #1c1c1c;");

            Scene scene = new Scene(scrollPane, 420, 600);
            settingsStage.setScene(scene);
            settingsStage.setTitle("Template Mod Settings");
            settingsStage.show();
        } else {
            if (settingsStage.isShowing()) {
                settingsStage.toFront();
            } else {
                settingsStage.show();
            }
        }
    }
}