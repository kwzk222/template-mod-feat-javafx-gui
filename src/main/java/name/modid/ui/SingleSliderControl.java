package name.modid.ui;

import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Circle;
import java.util.function.Consumer;

public class SingleSliderControl extends HBox {
    private final double min, max;
    private final int step;
    private double value;

    private final Rectangle track;
    private final Rectangle filled;
    private final Circle thumb;
    private final Label valueLabel;
    private Consumer<Double> onValueChanged;

    public SingleSliderControl(double min, double max, double initial, int step) {
        this.min = min;
        this.max = max;
        this.step = step;
        this.value = snap(initial);

        setSpacing(8);
        setAlignment(Pos.CENTER_LEFT);

        // label area consumed by caller; this control expects label to the left externally
        Pane sliderPane = new Pane();
        sliderPane.setPrefHeight(28);
        sliderPane.setPrefWidth(220);

        track = new Rectangle(0, 12, sliderPane.getPrefWidth(), 4);
        track.setFill(Color.web("#333"));

        filled = new Rectangle(0, 12, valueToScreen(value), 4);
        filled.setFill(Color.web(UIConstants.ACTIVE));

        thumb = new Circle(6, Color.web(UIConstants.ACTIVE));
        thumb.setCursor(Cursor.HAND);
        positionThumb();

        // ticks
        for (int i = (int) min; i <= (int) max; i++) {
            double x = valueToScreen((double) i);
            javafx.scene.shape.Line tick = new javafx.scene.shape.Line(x, 18, x, 22);
            tick.setStroke(Color.web("#555"));
            sliderPane.getChildren().add(tick);
        }

        sliderPane.getChildren().addAll(track, filled, thumb);

        // drag behavior
        thumb.setOnMouseDragged(e -> {
            double x = clamp(e.getX(), 0, sliderPane.getPrefWidth());
            double v = snap(screenToValue(x));
            setValue(v);
        });

        // value label (editable on double click)
        valueLabel = new Label(formatValue(value));
        valueLabel.setTextFill(Color.web(UIConstants.TEXT));
        valueLabel.setOnMouseClicked(e -> {
            if (e.getButton() == MouseButton.PRIMARY && e.getClickCount() == 2) {
                startEditValue();
            }
        });

        getChildren().addAll(sliderPane, valueLabel);
    }

    private void startEditValue() {
        TextField input = new TextField(String.valueOf((int) value));
        input.setPrefWidth(60);
        input.setStyle("-fx-background-color: " + UIConstants.BG + "; -fx-text-fill: " + UIConstants.TEXT + "; -fx-border-color: " + UIConstants.BORDER + "; -fx-border-radius:4; -fx-background-radius:4;");
        int index = getChildren().indexOf(valueLabel);
        getChildren().set(index, input);
        input.requestFocus();
        input.setOnAction(ae -> {
            try {
                int parsed = (int) Math.round(Double.parseDouble(input.getText()));
                if (parsed < min) parsed = (int) min;
                if (parsed > max) parsed = (int) max;
                setValue(parsed);
            } catch (NumberFormatException ignored) {}
            getChildren().set(index, valueLabel);
        });
        input.focusedProperty().addListener((obs, oldv, newv) -> {
            if (!newv) { // lost focus
                getChildren().set(index, valueLabel);
            }
        });
    }

    private String formatValue(double v) {
        return ((int) Math.round(v)) + "%";
    }

    private double valueToScreen(double v) {
        return (v - min) / (max - min) * 220.0;
    }

    private double screenToValue(double x) {
        return min + (x / 220.0) * (max - min);
    }

    private double snap(double val) {
        double snapped = Math.round(val / step) * step;
        if (snapped < min) snapped = min;
        if (snapped > max) snapped = max;
        return snapped;
    }

    private void positionThumb() {
        thumb.setCenterX(valueToScreen(value));
        thumb.setCenterY(14);
        filled.setWidth(valueToScreen(value));
    }

    public void setValue(double v) {
        this.value = snap(v);
        positionThumb();
        valueLabel.setText(formatValue(value));
        if (onValueChanged != null) onValueChanged.accept(value);
    }

    public double getValue() {
        return value;
    }

    public void setOnValueChanged(Consumer<Double> c) {
        this.onValueChanged = c;
    }

    private double clamp(double v, double a, double b) { return Math.max(a, Math.min(b, v)); }
}