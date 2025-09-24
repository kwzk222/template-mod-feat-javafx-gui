package name.modid.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Window;
import javafx.scene.Node;

public class SettingsPanel extends VBox {

    private final HBox header;
    private double dragOffsetX, dragOffsetY;
    private final VBox content;

    public SettingsPanel(String titleText) {

        // === PANEL STYLING ===
        setSpacing(0);
        setPadding(new Insets(0));
        setStyle("-fx-background-color: #1e1e1e; -fx-background-radius: 12; -fx-border-radius: 12; -fx-border-color: #e53935; -fx-border-width: 2;");

        // Clip to rounded corners
        Rectangle clip = new Rectangle();
        clip.widthProperty().bind(widthProperty());
        clip.heightProperty().bind(heightProperty());
        clip.setArcWidth(12);
        clip.setArcHeight(12);
        setClip(clip);

        // === HEADER ===
        Label title = new Label(titleText);
        title.setTextFill(Color.WHITE);
        title.setStyle("-fx-font-weight: bold; -fx-font-size: 14;");

        header = new HBox(title);
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(8));
        header.setStyle("-fx-background-color: #2a2a2a; -fx-background-radius: 12 12 0 0;");
        header.setPrefHeight(32);

        // Dragging logic (only via header)
        header.setOnMousePressed(e -> {
            Window window = getScene().getWindow();
            dragOffsetX = e.getScreenX() - window.getX();
            dragOffsetY = e.getScreenY() - window.getY();
        });
        header.setOnMouseDragged(e -> {
            Window window = getScene().getWindow();
            window.setX(e.getScreenX() - dragOffsetX);
            window.setY(e.getScreenY() - dragOffsetY);
        });

        // === CONTENT AREA ===
        content = new VBox(10);
        content.setPadding(new Insets(10));
        content.setAlignment(Pos.TOP_LEFT);

        // Add header + content
        getChildren().addAll(header, content);
    }

    // Add any setting row
    public void addSetting(Node row) {
        content.getChildren().add(row);
    }
}