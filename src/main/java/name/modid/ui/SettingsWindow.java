package name.modid.ui;

import javafx.scene.Scene;
import javafx.stage.Stage;

public class SettingsWindow {
    private static Stage stage;

    public static void init() {
        if (stage == null) {
            stage = new Stage();
            stage.setTitle("Settings");

            SettingsPanel panel = new SettingsPanel();
            Scene scene = new Scene(panel, 420, 500);

            // Load the external stylesheet
            try {
                String css = SettingsWindow.class.getResource("/style.css").toExternalForm();
                if (css != null) {
                    scene.getStylesheets().add(css);
                }
            } catch (Exception e) {
                System.err.println("Error loading stylesheet: " + e.getMessage());
            }

            stage.setScene(scene);
        }
    }

    public static void showWindow() {
        init();
        stage.show();
        stage.toFront();
    }

    public static Stage getStage() {
        init();
        return stage;
    }
}