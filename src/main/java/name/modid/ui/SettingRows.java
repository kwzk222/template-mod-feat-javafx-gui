package name.modid.ui;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;

public class SettingRows {

    /**
     * Creates a standard setting row with a fixed-width label on the left
     * and a control that fills the remaining space on the right.
     *
     * @param labelText The text for the label.
     * @param control   The UI control (e.g., a slider, toggle).
     * @return A formatted HBox containing the setting row.
     */
    public static HBox makeRow(String labelText, Node control) {
        Label label = new Label(labelText);
        label.setTextFill(Color.web(UIConstants.TEXT));
        label.setPrefWidth(120); // Fixed column width for alignment

        HBox.setHgrow(control, Priority.ALWAYS);

        HBox row = new HBox(8, label, control);
        row.setAlignment(Pos.CENTER_LEFT);
        return row;
    }

    /**
     * Creates a setting row specifically for a ComboBox, ensuring it's styled correctly.
     *
     * @param labelText The text for the label.
     * @param combo     The ComboBox to add.
     * @return A formatted HBox containing the ComboBox row.
     */
    public static HBox makeSelectRow(String labelText, ComboBox<String> combo) {
        combo.setStyle(
            "-fx-background-color: " + UIConstants.INACTIVE + ";" +
            "-fx-text-fill: " + UIConstants.TEXT + ";" +
            "-fx-border-color: " + UIConstants.BORDER + ";"
        );

        combo.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? "" : item);
                setTextFill(Color.web(UIConstants.TEXT));
            }
        });

        return makeRow(labelText, combo);
    }
}