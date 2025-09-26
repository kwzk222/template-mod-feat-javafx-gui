package name.modid.ui;

import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

public class ModuleSection extends VBox {
    private final HBox header;
    private final Button toggleButton;
    private final Button expandButton;
    private final VBox contentBox;

    private boolean expanded = false;
    private boolean enabled = false;

    public ModuleSection(String moduleName) {
        setSpacing(4);
        setFillWidth(true);

        // --- Header Row ---
        header = new HBox(8);
        header.setPadding(new Insets(4));
        header.setBackground(new Background(new BackgroundFill(Color.web("#1e1e1e"),
                new CornerRadii(6, 6, 0, 0, false), Insets.EMPTY)));
        header.setBorder(new Border(new BorderStroke(Color.DARKGRAY,
                BorderStrokeStyle.SOLID, new CornerRadii(6, 6, 0, 0, false), BorderWidths.DEFAULT)));

        // Toggle button (ON/OFF bar style)
        toggleButton = new Button(moduleName);
        toggleButton.setCursor(Cursor.HAND);
        toggleButton.setPrefWidth(180);
        updateToggleColor();
        toggleButton.setOnAction(e -> {
            enabled = !enabled;
            updateToggleColor();
        });

        // Expand/collapse arrow
        expandButton = new Button("▶");
        expandButton.setCursor(Cursor.HAND);
        expandButton.setOnAction(e -> toggleExpand());

        header.getChildren().addAll(toggleButton, expandButton);

        // --- Content area (settings) ---
        contentBox = new VBox(6);
        contentBox.setPadding(new Insets(6, 6, 6, 24));
        contentBox.setVisible(false);
        contentBox.setManaged(false);

        getChildren().addAll(header, contentBox);
    }

    private void updateToggleColor() {
        if (enabled) {
            toggleButton.setStyle("-fx-background-color: #b22222; -fx-text-fill: white;");
        } else {
            toggleButton.setStyle("-fx-background-color: #555555; -fx-text-fill: white;");
        }
    }

    private void toggleExpand() {
        expanded = !expanded;
        expandButton.setText(expanded ? "▼" : "▶");
        contentBox.setVisible(expanded);
        contentBox.setManaged(expanded);
    }

    // Add child settings (sliders, dropdowns, etc.)
    public void addSetting(javafx.scene.Node node) {
        contentBox.getChildren().add(node);
    }

    public boolean isEnabled() {
        return enabled;
    }
}