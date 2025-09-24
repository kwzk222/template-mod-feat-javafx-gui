package name.modid;

import name.modid.ui.SettingsWindow;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.util.Window;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import org.lwjgl.glfw.GLFW;

public class TemplateModClient implements ClientModInitializer {
    private static SettingsWindow settingsWindow;
    private static KeyBinding toggleGuiKeyBinding;
    private static boolean isGuiOpen = false;

    private static int lastWinX = Integer.MIN_VALUE;
    private static int lastWinY = Integer.MIN_VALUE;
    private static int lastWinW = Integer.MIN_VALUE;
    private static int lastWinH = Integer.MIN_VALUE;

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
            while (toggleGuiKeyBinding.wasPressed()) {
                isGuiOpen = !isGuiOpen;
                Platform.runLater(() -> {
                    if (isGuiOpen) {
                        settingsWindow.show();
                    } else {
                        settingsWindow.hide();
                    }
                });
            }

            if (!isGuiOpen) return;

            Window w = MinecraftClient.getInstance().getWindow();
            if (w == null) return;

            int winX = w.getX();
            int winY = w.getY();
            int winW = w.getWidth();
            int winH = w.getHeight();

            if (winX != lastWinX || winY != lastWinY || winW != lastWinW || winH != lastWinH) {
                lastWinX = winX;
                lastWinY = winY;
                lastWinW = winW;
                lastWinH = winH;
                Platform.runLater(() -> {
                    if (settingsWindow != null) {
                        settingsWindow.setPos(winX + 40, winY + 40);
                    }
                });
            }
        });
    }
}