package name.modid.ui;

import javafx.geometry.Pos;
import javafx.scene.control.Button;

public class TogglePill extends Button {
    private boolean selected;

    public TogglePill(boolean initial) {
        this.selected = initial;
        setPrefSize(64, 24);
        setMinSize(64, 24);
        setMaxSize(64, 24);
        setAlignment(Pos.CENTER);
        setFocusTraversable(false);
        updateStyle();

        setOnAction(e -> {
            selected = !selected;
            updateStyle();
        });
    }

    private void updateStyle() {
        String bg = selected ? UIConstants.ACTIVE : UIConstants.INACTIVE;
        setText(selected ? "ON" : "OFF");
        setStyle(
            "-fx-background-color: " + bg + ";" +
            "-fx-text-fill: " + UIConstants.TEXT + ";" +
            "-fx-background-radius: 12;" +
            "-fx-font-weight: bold;"
        );
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean sel) {
        this.selected = sel;
        updateStyle();
    }
}