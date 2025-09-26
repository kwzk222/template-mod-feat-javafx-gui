package name.modid.ui.components;

import javafx.scene.Cursor;
import javafx.scene.control.Button;

public class ToggleSwitch extends Button {
    private final String baseLabel;
    private boolean selected = false;

    public ToggleSwitch(String label) {
        this.baseLabel = label;
        setCursor(Cursor.HAND);
        setPrefWidth(160);

        updateState();

        setOnAction(e -> {
            selected = !selected;
            updateState();
        });
    }

    private void updateState() {
        if (selected) {
            setText(baseLabel + ": ON");
            setStyle("-fx-background-color: #8b0000; -fx-text-fill: white; -fx-background-radius: 12; -fx-font-weight: bold;");
        } else {
            setText(baseLabel + ": OFF");
            setStyle("-fx-background-color: #333333; -fx-text-fill: white; -fx-background-radius: 12; -fx-font-weight: bold;");
        }
    }

    public boolean isSelected() {
        return selected;
    }
}