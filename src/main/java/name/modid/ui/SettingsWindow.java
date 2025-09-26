package name.modid.ui;

import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.*;
import javafx.scene.paint.Color;
import name.modid.ui.components.DoubleSlider;
import name.modid.ui.components.SingleSlider;

public class SettingsWindow {

    private final Stage stage;

    public SettingsWindow() {
        stage = new Stage();
        stage.setTitle("Template Mod Settings");
        stage.initStyle(StageStyle.TRANSPARENT);  // no OS borders
        stage.setAlwaysOnTop(true);

        // === SETTINGS PANEL ===
        SettingsPanel panel = new SettingsPanel("Template Mod Settings");
        panel.setPrefSize(420, 360);

        // === TOGGLE ===
        ToggleButton toggle = new ToggleButton();
        toggle.setPrefWidth(60); // Smaller width
        toggle.setPrefHeight(24); // Smaller height
        toggle.setMaxSize(60, 24);
        toggle.setMinSize(60, 24);
        toggle.setAlignment(Pos.CENTER);
        toggle.setSelected(true);

        // Helper to update style based on state
        Runnable updateToggleStyle = () -> {
            if (toggle.isSelected()) {
                toggle.setText("ON");
                // Pill shape, red background
                toggle.setStyle("-fx-background-color: #e53935; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 12;");
            } else {
                toggle.setText("OFF");
                // Pill shape, dark background
                toggle.setStyle("-fx-background-color: #555; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 12;");
            }
        };

        // Set initial style on launch
        updateToggleStyle.run();

        // Add listener to update style on change
        toggle.selectedProperty().addListener((obs, oldVal, newVal) -> updateToggleStyle.run());

        panel.addSetting(SettingRows.makeToggleRow("Enabled", toggle));

        // === SINGLE SLIDER (Chance) ===
        SingleSlider chanceSlider = new SingleSlider(0, 100, 50, 1);
        panel.addSetting(SettingRows.makeSingleSliderRow("Chance", chanceSlider));

        // === DOUBLE SLIDER (Delay) ===
        DoubleSlider delaySlider = new DoubleSlider(0, 50, 7, 21, 1);
        panel.addSetting(SettingRows.makeDoubleSliderRow("Delay", delaySlider));

        // === DROPDOWN (Mode) ===
        ComboBox<String> combo = new ComboBox<>();
        combo.getItems().addAll("Alpha", "Beta", "Gamma");
        combo.setValue("Alpha");
        panel.addSetting(SettingRows.makeSelectRow("Mode", combo));

        // === SCENE ===
        Scene scene = new Scene(panel);
        scene.setFill(Color.TRANSPARENT);

        // Load the external stylesheet
        try {
            String css = getClass().getResource("/style.css").toExternalForm();
            scene.getStylesheets().add(css);
        } catch (Exception e) {
            System.err.println("Error loading stylesheet: " + e.getMessage());
        }

        stage.setScene(scene);
    }

    public void show() {
        stage.show();
        stage.toFront();
    }

    public void hide() {
        stage.hide();
    }

    public boolean isShowing() {
        return stage.isShowing();
    }

    public void setPos(double x, double y) {
        stage.setX(x);
        stage.setY(y);
    }
}