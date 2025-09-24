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
        ToggleButton toggle = new ToggleButton("ON");
        toggle.setPrefWidth(80);
        toggle.setPrefHeight(28);
        toggle.setMaxSize(80, 28);
        toggle.setMinSize(80, 28);
        toggle.setAlignment(Pos.CENTER);
        toggle.setSelected(true);

        toggle.selectedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                toggle.setText("ON");
                toggle.setStyle("-fx-background-color: #e53935; -fx-text-fill: white; -fx-font-weight: bold;");
            } else {
                toggle.setText("OFF");
                toggle.setStyle("-fx-background-color: #555; -fx-text-fill: white; -fx-font-weight: bold;");
            }
        });

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

        stage.setScene(scene);
    }

    public void show() { stage.show(); }
    public void hide() { stage.hide(); }
    public void setPos(double x, double y) {
        stage.setX(x);
        stage.setY(y);
    }
}