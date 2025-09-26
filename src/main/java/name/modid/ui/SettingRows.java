package name.modid.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.Cursor;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import name.modid.ui.components.DoubleSlider;
import name.modid.ui.components.SingleSlider;
import javafx.scene.Cursor;
import javafx.scene.control.ListCell;

public class SettingRows {

    public static HBox makeToggleRow(String name, ToggleButton toggle) {
        Label label = new Label(name);
        label.setTextFill(Color.WHITE);
        label.setPrefWidth(80);

        HBox row = new HBox(10, label, toggle);
        row.setAlignment(Pos.CENTER_LEFT);
        return row;
    }

    public static HBox makeSingleSliderRow(String name, SingleSlider slider) {
        Label label = new Label(name);
        label.setTextFill(Color.WHITE);
        label.setPrefWidth(80);

        Label valueLabel = new Label((int) slider.getValue() + "%");
        valueLabel.setTextFill(Color.WHITE);
        valueLabel.setCursor(Cursor.HAND);

        slider.setOnValueChanged(val -> {
            valueLabel.setText((int) (double) val + "%");
        });

        HBox row = new HBox(8, label, slider, valueLabel);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setPadding(new Insets(0, 0, 0, -12));

        valueLabel.setOnMouseClicked(e -> {
            TextField inputField = new TextField(String.valueOf((int) slider.getValue()));
            inputField.setPrefWidth(45);

            inputField.setOnAction(evt -> {
                try {
                    int input = Integer.parseInt(inputField.getText());
                    double clampedInput = Math.max(slider.getMin(), Math.min(slider.getMax(), input));
                    slider.setValue(clampedInput);
                } catch (NumberFormatException ignored) {}
                row.getChildren().set(row.getChildren().indexOf(inputField), valueLabel);
            });

            inputField.focusedProperty().addListener((obs, oldVal, newVal) -> {
                if (!newVal) {
                    try {
                        row.getChildren().set(row.getChildren().indexOf(inputField), valueLabel);
                    } catch (IllegalArgumentException ignored) {}
                }
            });

            row.getChildren().set(row.getChildren().indexOf(valueLabel), inputField);
            inputField.requestFocus();
        });

        return row;
    }

    public static HBox makeDoubleSliderRow(String name, DoubleSlider slider) {
        Label label = new Label(name);
        label.setTextFill(Color.WHITE);
        label.setPrefWidth(80);

        Label lowValueLabel = new Label(String.valueOf((int) slider.getLow()));
        lowValueLabel.setTextFill(Color.WHITE);
        lowValueLabel.setCursor(Cursor.HAND);

        Label separatorLabel = new Label("-");
        separatorLabel.setTextFill(Color.WHITE);

        Label highValueLabel = new Label(String.valueOf((int) slider.getHigh()));
        highValueLabel.setTextFill(Color.WHITE);
        highValueLabel.setCursor(Cursor.HAND);

        Label unitLabel = new Label("ticks");
        unitLabel.setTextFill(Color.WHITE);

        slider.addChangeListener(() -> {
            lowValueLabel.setText(String.valueOf((int) slider.getLow()));
            highValueLabel.setText(String.valueOf((int) slider.getHigh()));
        });

        HBox row = new HBox(8, label, slider, lowValueLabel, separatorLabel, highValueLabel, unitLabel);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setPadding(new Insets(0, 0, 0, -12));

        // Click-to-edit for low value
        lowValueLabel.setOnMouseClicked(e -> {
            TextField inputField = new TextField(lowValueLabel.getText());
            inputField.setPrefWidth(35);
            row.getChildren().set(row.getChildren().indexOf(lowValueLabel), inputField);
            inputField.requestFocus();

            Runnable commit = () -> {
                try {
                    int input = Integer.parseInt(inputField.getText());
                    slider.setLowValue(input);
                } catch (NumberFormatException ignored) {}
                if (row.getChildren().contains(inputField)) {
                    row.getChildren().set(row.getChildren().indexOf(inputField), lowValueLabel);
                }
            };

            inputField.setOnAction(evt -> commit.run());
            inputField.focusedProperty().addListener((obs, oldVal, newVal) -> {
                if (!newVal) commit.run();
            });
        });

        // Click-to-edit for high value
        highValueLabel.setOnMouseClicked(e -> {
            TextField inputField = new TextField(highValueLabel.getText());
            inputField.setPrefWidth(35);
            row.getChildren().set(row.getChildren().indexOf(highValueLabel), inputField);
            inputField.requestFocus();

            Runnable commit = () -> {
                try {
                    int input = Integer.parseInt(inputField.getText());
                    slider.setHighValue(input);
                } catch (NumberFormatException ignored) {}
                 if (row.getChildren().contains(inputField)) {
                    row.getChildren().set(row.getChildren().indexOf(inputField), highValueLabel);
                }
            };

            inputField.setOnAction(evt -> commit.run());
            inputField.focusedProperty().addListener((obs, oldVal, newVal) -> {
                if (!newVal) commit.run();
            });
        });

        return row;
    }

    public static HBox makeSelectRow(String name, ComboBox<String> combo) {
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
                setStyle("-fx-background-color: #2b2b2b; -fx-border-width: 0;"); // Unified background
            }
        });
        combo.setCellFactory(list -> new ListCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item);
                setTextFill(Color.WHITE);
                setStyle("-fx-background-color: #2b2b2b; -fx-border-width: 0;"); // Unified background
            }
        });

        HBox row = new HBox(10, label, combo);
        row.setAlignment(Pos.CENTER_LEFT);
        return row;
    }
}