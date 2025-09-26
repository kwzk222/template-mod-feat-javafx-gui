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
        setSpacing(2);
        setFillWidth(true);

        // --- Header Row ---
        header = new HBox(8);
        header.setPadding(new Insets(6));
        header.setBackground(new Background(new BackgroundFill(Color.web("#2b2b2b"),
                new CornerRadii(8), Insets.EMPTY)));
        header.setBorder(new Border(new BorderStroke(Color.web("#444444"),
                BorderStrokeStyle.SOLID, new CornerRadii(8), new BorderWidths(1))));

        // Toggle button (ON/OFF bar style)
        toggleButton = new Button(moduleName + ": OFF");
        toggleButton.setCursor(Cursor.HAND);
        toggleButton.setPrefWidth(200);
        toggleButton.setStyle("-fx-background-color: #555555; -fx-text-fill: white; -fx-background-radius: 6; -fx-font-weight: bold;");
        toggleButton.setOnAction(e -> {
            enabled = !enabled;
            updateToggleStyle(moduleName);
        });

        // Expand/collapse arrow
        expandButton = new Button("►"); // Use cleaner initial arrow
        expandButton.setCursor(Cursor.HAND);
        expandButton.setPrefWidth(24);
        expandButton.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-font-size: 12;");
        expandButton.setOnAction(e -> toggleExpand());

        header.getChildren().addAll(toggleButton, expandButton);

        // --- Content area (settings) ---
        contentBox = new VBox(6);
        contentBox.setPadding(new Insets(6, 6, 6, 24));
        contentBox.setVisible(false);
        contentBox.setManaged(false);

        getChildren().addAll(header, contentBox);
    }

    private void updateToggleStyle(String moduleName) {
        if (enabled) {
            toggleButton.setText(moduleName + ": ON");
            toggleButton.setStyle("-fx-background-color: #b22222; -fx-text-fill: white; -fx-background-radius: 6; -fx-font-weight: bold;");
        } else {
            toggleButton.setText(moduleName + ": OFF");
            toggleButton.setStyle("-fx-background-color: #555555; -fx-text-fill: white; -fx-background-radius: 6; -fx-font-weight: bold;");
        }
    }

    private void toggleExpand() {
        expanded = !expanded;
        expandButton.setText(expanded ? "▼" : "►");  // Use cleaner arrows
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