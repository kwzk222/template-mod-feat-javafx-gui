package name.modid.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
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

        slider.setOnValueChanged(val -> {
            valueLabel.setText((int) (double) val + "%");
        });

        HBox row = new HBox(8, label, slider, valueLabel); // Reduced spacing
        row.setAlignment(Pos.CENTER_LEFT);
        row.setPadding(new Insets(0, 0, 0, -12)); // Shift slider left
        return row;
    }

    public static HBox makeDoubleSliderRow(String name, DoubleSlider slider) {
        Label label = new Label(name);
        label.setTextFill(Color.WHITE);
        label.setPrefWidth(80);

        Label valueLabel = new Label((int)slider.getLow() + "-" + (int)slider.getHigh() + " ticks");
        valueLabel.setTextFill(Color.WHITE);

        slider.addChangeListener(() -> {
            valueLabel.setText((int)slider.getLow() + "-" + (int)slider.getHigh() + " ticks");
        });

        HBox row = new HBox(8, label, slider, valueLabel); // Reduced spacing
        row.setAlignment(Pos.CENTER_LEFT);
        row.setPadding(new Insets(0, 0, 0, -12)); // Shift slider left
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