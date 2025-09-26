package name.modid.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

public class ModuleSection extends VBox {
    private final HBox header;
    private final TogglePill togglePill;
    private final Button expandButton;
    private final VBox contentBox;

    private boolean expanded = false;

    public ModuleSection(String moduleName) {
        setSpacing(4);
        setFillWidth(true);

        header = new HBox();
        header.setSpacing(8);
        header.setPadding(new Insets(6));
        header.setAlignment(Pos.CENTER_LEFT);
        header.setBackground(new Background(new BackgroundFill(Color.web(UIConstants.BG),
                new CornerRadii(8), Insets.EMPTY)));
        header.setBorder(new Border(new BorderStroke(Color.web(UIConstants.BORDER),
                BorderStrokeStyle.SOLID, new CornerRadii(8), new BorderWidths(1))));

        // Left label
        javafx.scene.control.Label nameLabel = new javafx.scene.control.Label(moduleName);
        nameLabel.setTextFill(javafx.scene.paint.Color.web(UIConstants.TEXT));
        nameLabel.setPrefWidth(120); // fixed width so everything aligns
        nameLabel.setStyle("-fx-font-weight: bold;");

        // Toggle pill on the right of the label (matching style)
        togglePill = new TogglePill(false);

        // Expand/collapse arrow (fixed size, transparent background)
        expandButton = new Button("▼"); // starts closed -> show down arrow
        expandButton.setCursor(Cursor.HAND);
        expandButton.setPrefWidth(22);
        expandButton.setFocusTraversable(false);
        expandButton.setStyle("-fx-background-color: transparent; -fx-text-fill: " + UIConstants.TEXT + "; -fx-font-size: 12;");

        expandButton.setOnAction(e -> toggleExpand());

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        header.getChildren().addAll(nameLabel, spacer, togglePill, expandButton);

        // content area
        contentBox = new VBox(6);
        contentBox.setPadding(new Insets(6, 6, 6, 20));
        contentBox.setVisible(false);
        contentBox.setManaged(false);

        getChildren().addAll(header, contentBox);
    }

    private void toggleExpand() {
        expanded = !expanded;
        expandButton.setText(expanded ? "▲" : "▼"); // open -> up arrow; closed -> down arrow
        contentBox.setVisible(expanded);
        contentBox.setManaged(expanded);
    }

    public void addSetting(javafx.scene.Node node) {
        contentBox.getChildren().add(node);
    }

    public boolean isEnabled() {
        return togglePill.isSelected();
    }

    public void setEnabled(boolean value) {
        togglePill.setSelected(value);
    }
}