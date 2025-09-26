package name.modid.ui.components;

import javafx.scene.Cursor;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;

import java.util.function.Consumer;

public class SingleSlider extends Pane {
    private final double min, max;
    private final Rectangle track, highlight;
    private final Circle thumb;
    private double value;

    public SingleSlider(double min, double max, double initialValue, int snapStep) {
        this.min = min;
        this.max = max;
        this.value = initialValue;

        setPrefSize(200, 32);
        setCursor(Cursor.HAND);

        // Track
        track = new Rectangle(0, 14, getPrefWidth(), 4);
        track.setFill(Color.web("#555555")); // Unified inactive color

        // Highlight from 0 to thumb
        highlight = new Rectangle(0, 14, valueToScreen(value), 4);
        highlight.setFill(Color.web("#b22222")); // Unified active color

        // Thumb
        thumb = new Circle(6, Color.web("#b22222")); // Unified active color
        thumb.setCursor(Cursor.HAND);
        positionThumb();

        thumb.setOnMouseDragged(e -> {
            double newX = clamp(e.getX(), 0, getPrefWidth());
            value = snap(screenToValue(newX), snapStep);
            positionThumb();
            highlight.setWidth(valueToScreen(value));
            if (onValueChanged != null) onValueChanged.accept(value);
        });

        // Add nodes in correct rendering order (bottom to top)
        for (int i = (int) min; i <= max; i++) {
            double x = valueToScreen(i);
            Line tick = new Line(x, 22, x, 26);
            tick.setStroke(Color.web("#555"));
            getChildren().add(tick);
        }

        getChildren().add(track);
        getChildren().add(highlight);
        getChildren().add(thumb);
    }

    private void positionThumb() {
        thumb.setCenterX(valueToScreen(value));
        thumb.setCenterY(16);
    }

    private double valueToScreen(double v) {
        return (v - min) / (max - min) * getPrefWidth();
    }

    private double screenToValue(double x) {
        return min + (x / getPrefWidth()) * (max - min);
    }

    private double snap(double val, int step) {
        return Math.round(val / step) * step;
    }

    private double clamp(double val, double min, double max) {
        return Math.max(min, Math.min(max, val));
    }

    // Optional callback for value change
    private Consumer<Double> onValueChanged;
    public void setOnValueChanged(Consumer<Double> c) { onValueChanged = c; }
    public double getValue() { return value; }
}
