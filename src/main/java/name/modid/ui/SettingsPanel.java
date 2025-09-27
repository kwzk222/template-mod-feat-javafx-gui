package name.modid.ui;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;

import java.util.ArrayList;
import java.util.List;

public class SettingsPanel extends VBox {

    private final List<ModuleBar> modules = new ArrayList<>();

    public SettingsPanel() {
        this.setSpacing(8);
        this.setPadding(new Insets(10));
        this.setStyle("-fx-background-color: #1c1c1c; -fx-border-radius: 8; -fx-background-radius: 8;");

        // Example: Auto Attack Module
        addModule("Auto Attack", true, module -> {
            module.addDropdown("Target", "Player", "Mob", "All");
            module.addSlider("Delay", 0, 50, 10, 1, "ticks");
            module.addDoubleSlider("Chance", 0, 100, 10, 50, 1, "%");
        });

        addModule("Flight", false, module -> {
            module.addSlider("Speed", 0, 10, 2, 1, "x");
        });

        // Add more modules here...
    }

    private void addModule(String name, boolean active, ModuleConfigurer configurer) {
        ModuleBar module = new ModuleBar(name, active);
        configurer.configure(module);
        this.getChildren().add(module);
        modules.add(module);
    }

    @FunctionalInterface
    interface ModuleConfigurer {
        void configure(ModuleBar module);
    }

    // --------------------- ModuleBar ---------------------
    public static class ModuleBar extends VBox {
        private final VBox children;
        final HBox bar; // The main toggle bar
        private final Label arrow;
        private boolean open = false;
        private boolean isActive;

        public ModuleBar(String name, boolean active) {
            this.isActive = active;
            setSpacing(5);

            bar = new HBox();
            bar.setPadding(new Insets(8));
            bar.setAlignment(Pos.CENTER_LEFT);
            updateBarStyle();

            Label moduleLabel = new Label(name);
            moduleLabel.setTextFill(Color.WHITE);
            moduleLabel.setFont(Font.font(14));

            Region spacer = new Region();
            HBox.setHgrow(spacer, Priority.ALWAYS);

            arrow = new Label("▼");
            arrow.setTextFill(Color.WHITE);
            arrow.setFont(Font.font(16));

            bar.getChildren().addAll(moduleLabel, spacer, arrow);

            children = new VBox(5);
            children.setPadding(new Insets(5, 0, 0, 15));
            children.setVisible(open);
            children.setManaged(open);

            bar.setOnMouseClicked(e -> {
                if (e.getTarget() != arrow) {
                    isActive = !isActive;
                    updateBarStyle();
                }
            });

            arrow.setOnMouseClicked(e -> {
                open = !open;
                children.setVisible(open);
                children.setManaged(open);
                arrow.setText(open ? "▲" : "▼");
                e.consume();
            });

            getChildren().addAll(bar, children);
        }

        private void updateBarStyle() {
            String style = isActive
                ? "-fx-background-color: red; -fx-background-radius: 8;"
                : "-fx-background-color: #444444; -fx-background-radius: 8;";
            bar.setStyle(style);
        }

        public void addDropdown(String name, String... options) {
            DropdownControl dropdown = new DropdownControl(name, options);
            children.getChildren().add(dropdown);
        }

        public void addSlider(String name, double min, double max, double initial, double step, String unit) {
            SliderControl control = new SliderControl(name, min, max, initial, step, unit);
            children.getChildren().add(control);
        }

        public void addDoubleSlider(String name, double min, double max, double low, double high, double step, String unit) {
            DoubleSliderControl control = new DoubleSliderControl(name, min, max, low, high, step, unit);
            children.getChildren().add(control);
        }
    }

    // --------------------- DropdownControl ---------------------
    public static class DropdownControl extends HBox {
        public DropdownControl(String name, String... options) {
            super(10);
            setAlignment(Pos.CENTER_LEFT);
            setPadding(new Insets(5, 10, 5, 10));

            Label label = new Label(name);
            label.setTextFill(Color.WHITE);
            label.setFont(Font.font(14));

            ComboBox<String> dropdown = new ComboBox<>();
            dropdown.getItems().addAll(options);
            if (options.length > 0) {
                dropdown.setValue(options[0]);
            }

            String baseStyle = "-fx-background-color: #2a2a2a; -fx-text-fill: white;";
            String hoverStyle = "-fx-background-color: red; -fx-text-fill: white;";

            dropdown.setStyle("-fx-background-color: #2a2a2a; -fx-font-size: 13px; -fx-border-width: 0;");

            dropdown.setCellFactory(lv -> new ListCell<>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                        setStyle(baseStyle);
                    } else {
                        setText(item);
                        setStyle(baseStyle);
                        hoverProperty().addListener((obs, wasHovered, isNowHovered) -> {
                            setStyle(isNowHovered ? hoverStyle : baseStyle);
                        });
                    }
                }
            });

            dropdown.setButtonCell(new ListCell<>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                    } else {
                        setText(item);
                        setTextFill(Color.WHITE);
                        setStyle("-fx-background-color: #2a2a2a;");
                    }
                }
            });

            Region spacer = new Region();
            HBox.setHgrow(spacer, Priority.ALWAYS);
            getChildren().addAll(label, spacer, dropdown);
        }
    }

    // --------------------- SliderControl ---------------------
    public static class SliderControl extends HBox {
        private final Slider slider;
        private final TextField valueBox;

        public SliderControl(String name, double min, double max, double initial, double step, String unit) {
            super(10);
            setAlignment(Pos.CENTER_LEFT);
            setPadding(new Insets(5, 10, 5, 10));

            Label label = new Label(name);
            label.setTextFill(Color.WHITE);
            label.setFont(Font.font(14));

            slider = new Slider(min, max, initial);
            slider.setBlockIncrement(step);

            StackPane sliderPane = new StackPane();
            Region trackBackground = new Region();
            trackBackground.setStyle("-fx-background-color: #444; -fx-background-radius: 2;");
            trackBackground.setPrefHeight(4);

            Region trackFill = new Region();
            trackFill.setStyle("-fx-background-color: red; -fx-background-radius: 2;");
            trackFill.setPrefHeight(4);
            StackPane.setAlignment(trackFill, Pos.CENTER_LEFT);

            slider.setStyle("-fx-control-inner-background: transparent;");

            sliderPane.getChildren().addAll(trackBackground, trackFill, slider);
            HBox.setHgrow(sliderPane, Priority.ALWAYS);

            slider.widthProperty().addListener((obs, oldW, newW) -> {
                if (newW.doubleValue() > 0) {
                    trackFill.prefWidthProperty().bind(
                        slider.valueProperty().subtract(min).divide(max - min).multiply(newW.doubleValue())
                    );
                }
            });

            valueBox = new TextField(String.format("%.0f", initial));
            valueBox.setStyle("-fx-background-color: #2a2a2a; -fx-text-fill: white; -fx-border-color: #444;");
            valueBox.setPrefWidth(50);

            slider.valueProperty().addListener((obs, oldVal, newVal) -> {
                valueBox.setText(String.format("%.0f", newVal.doubleValue()));
            });

            valueBox.setOnAction(e -> {
                try {
                    double val = Double.parseDouble(valueBox.getText());
                    if (val < min) val = min;
                    if (val > max) val = max;
                    slider.setValue(val);
                } catch (NumberFormatException ignored) {
                    valueBox.setText(String.format("%.0f", slider.getValue()));
                }
            });

            getChildren().addAll(label, sliderPane, valueBox);
        }
    }

    // --------------------- DoubleSliderControl ---------------------
    public static class DoubleSliderControl extends VBox {
        private final RangeSlider rangeSlider;
        private final TextField minBox;
        private final TextField maxBox;

        public DoubleSliderControl(String name, double min, double max, double low, double high, double step, String unit) {
            super(5);
            setPadding(new Insets(5, 10, 5, 10));

            Label label = new Label(name);
            label.setTextFill(Color.WHITE);
            label.setFont(Font.font(14));

            rangeSlider = new RangeSlider(min, max, low, high, step);

            minBox = new TextField(String.format("%.0f", low));
            maxBox = new TextField(String.format("%.0f", high));

            String boxStyle = "-fx-background-color: #2a2a2a; -fx-text-fill: white; -fx-border-color: #444;";
            minBox.setStyle(boxStyle);
            maxBox.setStyle(boxStyle);
            minBox.setPrefWidth(50);
            maxBox.setPrefWidth(50);

            rangeSlider.lowValueProperty().addListener((obs, oldV, newV) -> minBox.setText(String.format("%.0f", newV.doubleValue())));
            rangeSlider.highValueProperty().addListener((obs, oldV, newV) -> maxBox.setText(String.format("%.0f", newV.doubleValue())));

            minBox.setOnAction(e -> {
                try {
                    double val = Double.parseDouble(minBox.getText());
                    rangeSlider.setLowValue(val);
                } catch (NumberFormatException ignored) {}
            });

            maxBox.setOnAction(e -> {
                try {
                    double val = Double.parseDouble(maxBox.getText());
                    rangeSlider.setHighValue(val);
                } catch (NumberFormatException ignored) {}
            });

            HBox valueRow = new HBox(10, minBox, maxBox);
            valueRow.setAlignment(Pos.CENTER_RIGHT);

            HBox topRow = new HBox(10, label, new Region(), valueRow);
            HBox.setHgrow(topRow.getChildren().get(1), Priority.ALWAYS);
            topRow.setAlignment(Pos.CENTER_LEFT);

            getChildren().addAll(topRow, rangeSlider);
        }
    }

    // --------------------- RangeSlider (for DoubleSliderControl) ---------------------
    public static class RangeSlider extends Region {
        private final DoubleProperty lowValue = new SimpleDoubleProperty();
        private final DoubleProperty highValue = new SimpleDoubleProperty();
        private final double min, max, step;

        private final Region track = new Region();
        private final Region range = new Region();
        private final Thumb lowThumb = new Thumb();
        private final Thumb highThumb = new Thumb();
        private Thumb selectedThumb = null;

        public RangeSlider(double min, double max, double low, double high, double step) {
            this.min = min;
            this.max = max;
            this.step = step;
            setLowValue(low);
            setHighValue(high);

            track.setStyle("-fx-background-color: #444; -fx-background-radius: 3;");
            range.setStyle("-fx-background-color: red; -fx-background-radius: 3;");

            getChildren().addAll(track, range, lowThumb, highThumb);

            lowValue.addListener((obs, oldV, newV) -> requestLayout());
            highValue.addListener((obs, oldV, newV) -> requestLayout());

            setUpMouseEvents();
        }

        private void setUpMouseEvents() {
            for (Thumb thumb : List.of(lowThumb, highThumb)) {
                thumb.setOnMousePressed(e -> selectedThumb = thumb);
                thumb.setOnMouseDragged(e -> {
                    if (selectedThumb != null) {
                        double mouseX = e.getX() + selectedThumb.getLayoutX();
                        double newValue = screenToValue(mouseX);

                        if (selectedThumb == lowThumb) {
                            setLowValue(Math.min(newValue, getHighValue()));
                        } else {
                            setHighValue(Math.max(newValue, getLowValue()));
                        }
                    }
                });
                thumb.setOnMouseReleased(e -> selectedThumb = null);
            }
        }

        @Override
        protected void layoutChildren() {
            double trackHeight = 6;
            double trackY = (getHeight() - trackHeight) / 2;
            track.resizeRelocate(0, trackY, getWidth(), trackHeight);

            double lowThumbX = valueToScreen(getLowValue());
            double highThumbX = valueToScreen(getHighValue());

            range.resizeRelocate(lowThumbX, trackY, highThumbX - lowThumbX, trackHeight);

            double thumbSize = 16;
            lowThumb.resizeRelocate(lowThumbX - thumbSize / 2, (getHeight() - thumbSize) / 2, thumbSize, thumbSize);
            highThumb.resizeRelocate(highThumbX - thumbSize / 2, (getHeight() - thumbSize) / 2, thumbSize, thumbSize);
        }

        private double screenToValue(double screen) {
            double rawValue = (screen / getWidth()) * (max - min) + min;
            return Math.max(min, Math.min(max, Math.round(rawValue / step) * step));
        }

        private double valueToScreen(double value) {
            return (value - min) / (max - min) * getWidth();
        }

        public double getLowValue() { return lowValue.get(); }
        public void setLowValue(double val) { lowValue.set(Math.max(min, Math.min(val, max))); }
        public DoubleProperty lowValueProperty() { return lowValue; }

        public double getHighValue() { return highValue.get(); }
        public void setHighValue(double val) { highValue.set(Math.max(min, Math.min(val, max))); }
        public DoubleProperty highValueProperty() { return highValue; }

        private static class Thumb extends Region {
            Thumb() {
                setPrefSize(16, 16);
                setStyle("-fx-background-color: #ddd; -fx-background-radius: 8; -fx-border-color: #aaa; -fx-border-width: 1; -fx-border-radius: 8;");
            }
        }
    }
}