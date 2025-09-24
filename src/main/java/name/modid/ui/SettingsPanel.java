package name.modid.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class SettingsPanel extends StackPane {

    private final VBox content;
    private final HBox header;
    private double dragOffsetX, dragOffsetY;

    public SettingsPanel(String title) {
        setStyle("-fx-background-color: #1e1e1e; "
               + "-fx-background-radius: 12; "
               + "-fx-border-radius: 12; "
               + "-fx-border-color: #e53935; "
               + "-fx-border-width: 2;");

        setPrefSize(400, 300);

        // Header
        Label titleLabel = new Label(title);
        titleLabel.setTextFill(Color.WHITE);
        titleLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14;");

        header = new HBox(titleLabel);
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(8));
        header.setPrefHeight(32);
        header.prefWidthProperty().bind(widthProperty());
        header.setStyle("-fx-background-color: #2a2a2a; "
                      + "-fx-background-radius: 12 12 0 0;");

        // Make panel draggable only from header
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

        // Content
        content = new VBox(10);
        content.setPadding(new Insets(10));
        content.setLayoutY(header.getPrefHeight());
        content.prefWidthProperty().bind(widthProperty());

        getChildren().addAll(header, content);

        Rectangle clip = new Rectangle();
        clip.setArcWidth(12);
        clip.setArcHeight(12);
        clip.widthProperty().bind(widthProperty());
        clip.heightProperty().bind(heightProperty());
        setClip(clip);
    }

    // API to add different setting rows
    public void addSetting(Node settingRow) {
        content.getChildren().add(settingRow);
    }
}
