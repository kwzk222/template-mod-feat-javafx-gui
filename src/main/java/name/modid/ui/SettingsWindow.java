package name.modid.ui;

import javafx.scene.Scene;
import javafx.stage.Stage;

public class SettingsWindow {

    private static Stage stage;

    public static boolean isOpen() {
        return stage != null && stage.isShowing();
    }

    public static void show() {
        if (stage == null) {
            stage = new Stage();
            stage.setTitle("Template Mod Settings");

            SettingsPanel panel = new SettingsPanel();
            Scene scene = new Scene(panel, 400, 600);
            stage.setScene(scene);

            stage.setResizable(true);
        }
        stage.show();
        stage.toFront();
    }

    // Method to match the client call
    public static void showWindow() {
        show();
    }

    // Adding getStage for completeness, as the old client code used it.
    public static Stage getStage() {
        if (stage == null) {
            show(); // Ensure stage is initialized
        }
        return stage;
    }
}