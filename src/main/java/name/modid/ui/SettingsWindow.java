package name.modid.ui;

import javafx.scene.Scene;
import javafx.stage.Stage;

public class SettingsWindow {

    private static Stage stage;

    public static boolean isShowing() {
        return stage != null && stage.isShowing();
    }

    public static void showWindow() {
        if (stage == null) {
            stage = new Stage();
            stage.setTitle("Template Mod Settings");

            // Your main panel
            SettingsPanel panel = new SettingsPanel();

            Scene scene = new Scene(panel, 400, 600); // Width x Height
            stage.setScene(scene);

            stage.setResizable(true);
        }
        stage.show();
        stage.toFront();
    }

    public static void hideWindow() {
        if (stage != null) {
            stage.hide();
        }
    }
}