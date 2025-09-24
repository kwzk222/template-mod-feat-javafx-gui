package name.modid.ui;

import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import name.modid.ui.components.DoubleSlider;
import name.modid.ui.components.SingleSlider;

public class SettingsWindow {

    private final Stage stage;

    public SettingsWindow() {
        stage = new Stage();
        stage.setTitle("Template Mod Settings");
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setAlwaysOnTop(true);

        SettingsPanel panel = new SettingsPanel("Template Mod Settings");
        panel.getStyleClass().add("settings-root");


        // Toggle
        ToggleButton toggle = new ToggleButton("ON");
        toggle.setSelected(true);
        panel.addSetting(makeToggleRow("Enabled", toggle));

        // Single slider (Chance)
        SingleSlider chanceSlider = new SingleSlider(0, 100, 50, 1);
        panel.addSetting(makeSingleSliderRow("Chance", chanceSlider));

        // Double slider (Delay)
        DoubleSlider delaySlider = new DoubleSlider(0, 50, 7, 21, 1);
        panel.addSetting(makeDoubleSliderRow("Delay", delaySlider));

        // Select box
        ComboBox<String> combo = new ComboBox<>();
        combo.getItems().addAll("Alpha", "Beta", "Gamma");
        combo.setValue("Alpha");
        panel.addSetting(makeSelectRow("Mode", combo));

        Scene scene = new Scene(panel);
        scene.setFill(Color.TRANSPARENT);
        // Assuming style.css is still relevant and accessible
        // scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());

        stage.setScene(scene);
        stage.setWidth(420);
        stage.setHeight(360);
    }

    public void show() {
        stage.show();
    }

    public void hide() {
        stage.hide();
    }

    public boolean isShowing() {
        return stage.isShowing();
    }

    private HBox makeToggleRow(String name, ToggleButton toggle) {
        Label label = new Label(name);
        label.setTextFill(Color.WHITE);
        label.setPrefWidth(80);

        toggle.setPrefWidth(80);
        toggle.setPrefHeight(28);
        toggle.setMaxSize(80, 28);
        toggle.setMinSize(80, 28);
        toggle.setAlignment(Pos.CENTER);
        toggle.setCursor(Cursor.HAND);

        toggle.selectedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                toggle.setText("ON");
                toggle.setStyle("-fx-background-color: #e53935; -fx-text-fill: white; -fx-font-weight: bold;");
            } else {
                toggle.setText("OFF");
                toggle.setStyle("-fx-background-color: #555; -fx-text-fill: white; -fx-font-weight: bold;");
            }
        });

        // Set initial state
        if (toggle.isSelected()) {
            toggle.setText("ON");
            toggle.setStyle("-fx-background-color: #e53935; -fx-text-fill: white; -fx-font-weight: bold;");
        } else {
            toggle.setText("OFF");
            toggle.setStyle("-fx-background-color: #555; -fx-text-fill: white; -fx-font-weight: bold;");
        }


        HBox row = new HBox(10, label, toggle);
        row.setAlignment(Pos.CENTER_LEFT);
        return row;
    }

    private HBox makeSingleSliderRow(String name, SingleSlider slider) {
        Label label = new Label(name);
        label.setTextFill(Color.WHITE);
        label.setPrefWidth(80);
        Label valueLabel = new Label((int) slider.getValue() + "%");
        valueLabel.setTextFill(Color.WHITE);
        slider.setOnValueChanged(val -> {
            valueLabel.setText((int) val.doubleValue() + "%");
        });
        HBox row = new HBox(10, label, slider, valueLabel);
        row.setAlignment(Pos.CENTER_LEFT);
        return row;
    }

    private HBox makeDoubleSliderRow(String name, DoubleSlider slider) {
        Label label = new Label(name);
        label.setTextFill(Color.WHITE);
        label.setPrefWidth(80);
        Label valueLabel = new Label((int)slider.getLow() + "-" + (int)slider.getHigh() + " ticks");
        valueLabel.setTextFill(Color.WHITE);
        slider.addChangeListener(() -> valueLabel.setText((int)slider.getLow() + "-" + (int)slider.getHigh() + " ticks"));
        HBox row = new HBox(10, label, slider, valueLabel);
        row.setAlignment(Pos.CENTER_LEFT);
        return row;
    }

    private HBox makeSelectRow(String name, ComboBox<String> combo) {
        Label label = new Label(name);
        label.setTextFill(Color.WHITE);
        label.setPrefWidth(80);

        combo.setCursor(Cursor.HAND);

        combo.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item);
                setTextFill(Color.WHITE);
                setStyle("-fx-background-color: #333; -fx-border-width: 0;");
            }
        });
        combo.setCellFactory(list -> new ListCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item);
                setTextFill(Color.WHITE);
                setStyle("-fx-background-color: #222; -fx-border-width: 0;");
            }
        });

        HBox row = new HBox(10, label, combo);
        row.setAlignment(Pos.CENTER_LEFT);
        return row;
    }
}