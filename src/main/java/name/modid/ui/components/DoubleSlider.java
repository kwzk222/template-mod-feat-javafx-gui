package name.modid.ui.components;

import javafx.scene.Cursor;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.List;

public class DoubleSlider extends Pane {
    private final double min, max;
    private final Rectangle track;
    private final Rectangle rangeHighlight;
    private final Circle thumbA, thumbB;

    private double valueA, valueB;

    private final List<Runnable> listeners = new ArrayList<>();

    public DoubleSlider(double min, double max, double initialA, double initialB, int snapStep) {
        this.min = min;
        this.max = max;
        this.valueA = initialA;
        this.valueB = initialB;

        setPrefSize(200, 32);
        setCursor(Cursor.HAND);

        track = new Rectangle(0, 14, getPrefWidth(), 4);
        track.setFill(Color.web("#333")); // dark track

        rangeHighlight = new Rectangle();
        rangeHighlight.setY(track.getY());
        rangeHighlight.setHeight(track.getHeight());
        rangeHighlight.setFill(Color.web("#e53935")); // red

        thumbA = createThumb(snapStep);
        thumbB = createThumb(snapStep);

        // Add nodes in correct rendering order (bottom to top)
        for (int i = (int) min; i <= max; i++) {
            double x = valueToScreen(i);
            Line tick = new Line(x, 22, x, 26);
            tick.setStroke(Color.web("#555"));
            getChildren().add(tick);
        }

        getChildren().add(track);
        getChildren().add(rangeHighlight);
        getChildren().add(thumbA);
        getChildren().add(thumbB);

        positionThumbs();
    }

    private Circle createThumb(int snapStep) {
        Circle c = new Circle(6, Color.web("#e53935")); // red accent
        c.setCursor(Cursor.HAND);

        c.setOnMouseDragged(e -> {
            double newX = clamp(e.getX(), 0, getPrefWidth());
            double snapped = snap(screenToValue(newX), snapStep);
            if (c == thumbA) valueA = snapped;
            else valueB = snapped;
            positionThumbs();
        });

        return c;
    }

    private void positionThumbs() {
        thumbA.setCenterX(valueToScreen(valueA));
        thumbA.setCenterY(16);
        thumbB.setCenterX(valueToScreen(valueB));
        thumbB.setCenterY(16);
        updateRangeHighlight();
        listeners.forEach(Runnable::run);
    }

    private void updateRangeHighlight() {
        double x1 = valueToScreen(valueA);
        double x2 = valueToScreen(valueB);
        double left = Math.min(x1, x2);
        double width = Math.abs(x2 - x1);
        rangeHighlight.setX(left);
        rangeHighlight.setWidth(width);
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

    public void addChangeListener(Runnable listener) {
        listeners.add(listener);
    }

    public double getLow() { return Math.min(valueA, valueB); }
    public double getHigh() { return Math.max(valueA, valueB); }
    public double getRandomInRange() {
        return getLow() + Math.random() * (getHigh() - getLow());
    }
}
