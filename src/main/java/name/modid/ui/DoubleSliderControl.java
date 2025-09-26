package name.modid.ui;

import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class DoubleSliderControl extends HBox {
    private final double min, max;
    private final int step;
    private double a, b; // values for two thumbs (independent)

    private final Rectangle track;
    private final Rectangle rangeFill;
    private final Circle thumbA, thumbB;
    private final Label valueLabel;
    private final Pane pane;

    private final List<Runnable> listeners = new ArrayList<>();
    private Consumer<double[]> onValueChanged;

    public DoubleSliderControl(double min, double max, double initialA, double initialB, int step) {
        this.min = min; this.max = max; this.step = step;
        this.a = snap(initialA); this.b = snap(initialB);

        setSpacing(8);
        setAlignment(Pos.CENTER_LEFT);

        pane = new Pane();
        pane.setPrefSize(220, 28);

        track = new Rectangle(0, 12, pane.getPrefWidth(), 4);
        track.setFill(Color.web("#333"));

        rangeFill = new Rectangle(0, 12, 0, 4);
        rangeFill.setFill(Color.web(UIConstants.ACTIVE));

        thumbA = new Circle(6, Color.web(UIConstants.ACTIVE));
        thumbA.setCursor(Cursor.HAND);
        thumbB = new Circle(6, Color.web(UIConstants.ACTIVE));
        thumbB.setCursor(Cursor.HAND);

        // ticks
        for (int i = (int) min; i <= (int) max; i++) {
            double x = valueToScreen((double) i);
            javafx.scene.shape.Line tick = new javafx.scene.shape.Line(x, 18, x, 22);
            tick.setStroke(Color.web("#555"));
            pane.getChildren().add(tick);
        }

        pane.getChildren().addAll(track, rangeFill, thumbA, thumbB);
        positionThumbs();

        thumbA.setOnMouseDragged(e -> {
            double x = clamp(e.getX(), 0, pane.getPrefWidth());
            a = snap(screenToValue(x));
            positionThumbs();
            notifyChange();
        });
        thumbB.setOnMouseDragged(e -> {
            double x = clamp(e.getX(), 0, pane.getPrefWidth());
            b = snap(screenToValue(x));
            positionThumbs();
            notifyChange();
        });

        // value label (editable on double click)
        valueLabel = new Label(formatValues());
        valueLabel.setTextFill(Color.web(UIConstants.TEXT));
        valueLabel.setOnMouseClicked(e -> {
            if (e.getButton() == MouseButton.PRIMARY && e.getClickCount() == 2) {
                startEdit();
            }
        });

        getChildren().addAll(pane, valueLabel);
    }

    private void startEdit() {
        TextField tf = new TextField((int)getLow() + "-" + (int)getHigh());
        tf.setPrefWidth(80);
        tf.setStyle("-fx-background-color:" + UIConstants.BG + "; -fx-text-fill:" + UIConstants.TEXT + "; -fx-border-color:" + UIConstants.BORDER + "; -fx-border-radius:4; -fx-background-radius:4;");
        int idx = getChildren().indexOf(valueLabel);
        getChildren().set(idx, tf);
        tf.requestFocus();

        tf.setOnAction(ae -> {
            try {
                String txt = tf.getText().trim();
                String[] parts = txt.split("\\s*-\\s*");
                if (parts.length == 2) {
                    int v1 = Integer.parseInt(parts[0]);
                    int v2 = Integer.parseInt(parts[1]);
                    v1 = clampInt(v1, (int)min, (int)max);
                    v2 = clampInt(v2, (int)min, (int)max);
                    // assign directly; thumbs independent but display will always be min/max
                    a = snap(v1);
                    b = snap(v2);
                    positionThumbs();
                    notifyChange();
                }
            } catch (NumberFormatException ignored) {}
            getChildren().set(idx, valueLabel);
            valueLabel.setText(formatValues());
        });

        tf.focusedProperty().addListener((obs, oldv, newv) -> {
            if (!newv) {
                getChildren().set(getChildren().indexOf(tf), valueLabel);
                valueLabel.setText(formatValues());
            }
        });
    }

    private int clampInt(int v, int lo, int hi) { return Math.max(lo, Math.min(hi, v)); }

    private String formatValues() {
        int low = (int)getLow();
        int high = (int)getHigh();
        return low + " - " + high + " ticks";
    }

    private void positionThumbs() {
        double xA = valueToScreen(a);
        double xB = valueToScreen(b);
        thumbA.setCenterX(xA); thumbA.setCenterY(14);
        thumbB.setCenterX(xB); thumbB.setCenterY(14);

        double left = Math.min(xA, xB);
        double width = Math.abs(xB - xA);
        rangeFill.setX(left);
        rangeFill.setWidth(width);

        valueLabel.setText(formatValues());
    }

    private double valueToScreen(double v) {
        return (v - min) / (max - min) * pane.getPrefWidth();
    }

    private double screenToValue(double x) {
        return min + (x / pane.getPrefWidth()) * (max - min);
    }

    private double snap(double val) {
        double s = Math.round(val / step) * step;
        if (s < min) s = min;
        if (s > max) s = max;
        return s;
    }

    private double clamp(double v, double lo, double hi) { return Math.max(lo, Math.min(hi, v)); }

    public double getLow() { return Math.min(a,b); }
    public double getHigh() { return Math.max(a,b); }

    private void notifyChange() {
        valueLabel.setText(formatValues());
        if (onValueChanged != null) onValueChanged.accept(new double[]{getLow(), getHigh()});
        for (Runnable r : listeners) r.run();
    }

    public void addChangeListener(Runnable r) { listeners.add(r); }

    public void setOnValueChanged(Consumer<double[]> c) { this.onValueChanged = c; }
}