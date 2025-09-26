package name.modid.ui;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

import java.util.ArrayList;
import java.util.List;

public class SettingsPanel extends VBox {

    private final List<ModuleBar> modules = new ArrayList<>();

    public SettingsPanel() {
        this.setSpacing(8);
        this.setPadding(new Insets(10));
        this.setStyle("-fx-background-color: #1c1c1c; -fx-border-radius: 8; -fx-background-radius: 8;");

        // Example: Auto Attack Module
        addModule("Auto Attack", module -> {
            module.addToggle("Enabled", true);
            module.addSlider("Delay", 0, 50, 10, 1, "ticks");
            module.addDoubleSlider("Chance", 0, 100, 10, 50, 5, "%");
        });

        // Add more modules here...
    }

    private void addModule(String name, ModuleConfigurer configurer) {
        ModuleBar module = new ModuleBar(name);
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
        private final HBox header;
        private final Label nameLabel;
        private final Button toggleButton;
        private final Button arrowButton;
        private final VBox content;
        private boolean expanded = false;

        public ModuleBar(String moduleName) {
            this.setSpacing(4);
            this.setPadding(new Insets(4));
            this.setStyle("-fx-background-color: #2a2a2a; -fx-border-radius: 6; -fx-background-radius: 6;");

            // Header
            header = new HBox();
            header.setSpacing(8);
            header.setPadding(new Insets(4));

            nameLabel = new Label(moduleName);
            nameLabel.setTextFill(Color.WHITE);
            nameLabel.setFont(new Font("Arial", 14));

            toggleButton = createToggleButton(false);
            arrowButton = new Button("▼");
            arrowButton.setFont(Font.font(12));
            arrowButton.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-padding: 0;");
            arrowButton.setOnAction(e -> toggleExpand());

            header.getChildren().addAll(nameLabel, toggleButton, arrowButton);

            content = new VBox();
            content.setSpacing(6);
            content.setPadding(new Insets(4));
            content.setVisible(false);

            this.getChildren().addAll(header, content);
        }

        private void toggleExpand() {
            expanded = !expanded;
            content.setVisible(expanded);
            arrowButton.setText(expanded ? "▲" : "▼");
        }

        public void addToggle(String name, boolean initial) {
            AutoToggle toggle = new AutoToggle(name, initial);
            content.getChildren().add(toggle);
        }

        public void addSlider(String name, int min, int max, int initial, int step, String suffix) {
            SliderControl slider = new SliderControl(name, min, max, initial, step, suffix);
            content.getChildren().add(slider);
        }

        public void addDoubleSlider(String name, int min, int max, int low, int high, int step, String suffix) {
            DoubleSliderControl slider = new DoubleSliderControl(name, min, max, low, high, step, suffix);
            content.getChildren().add(slider);
        }

        private Button createToggleButton(boolean initial) {
            Button btn = new Button(initial ? "ON" : "OFF");
            btn.setStyle("-fx-background-radius: 12; -fx-background-color: " + (initial ? "#ff3333" : "#555555") + "; -fx-text-fill: white;");
            btn.setPrefWidth(50);
            btn.setOnAction(e -> {
                boolean on = btn.getText().equals("OFF");
                btn.setText(on ? "ON" : "OFF");
                btn.setStyle("-fx-background-radius: 12; -fx-background-color: " + (on ? "#ff3333" : "#555555") + "; -fx-text-fill: white;");
            });
            return btn;
        }
    }

    // --------------------- AutoToggle ---------------------
    public static class AutoToggle extends HBox {
        private final Label nameLabel;
        private final Button toggleButton;

        public AutoToggle(String name, boolean initial) {
            this.setSpacing(8);

            nameLabel = new Label(name);
            nameLabel.setTextFill(Color.WHITE);
            nameLabel.setFont(Font.font(13));

            toggleButton = new Button(initial ? "ON" : "OFF");
            toggleButton.setPrefWidth(50);
            toggleButton.setStyle("-fx-background-radius: 12; -fx-background-color: " + (initial ? "#ff3333" : "#555555") + "; -fx-text-fill: white;");
            toggleButton.setOnAction(e -> {
                boolean on = toggleButton.getText().equals("OFF");
                toggleButton.setText(on ? "ON" : "OFF");
                toggleButton.setStyle("-fx-background-radius: 12; -fx-background-color: " + (on ? "#ff3333" : "#555555") + "; -fx-text-fill: white;");
            });

            this.getChildren().addAll(nameLabel, toggleButton);
        }
    }

    // --------------------- SliderControl ---------------------
    public static class SliderControl extends HBox {
        private final Label nameLabel;
        private final Slider slider;
        private final Label valueLabel;
        private final String suffix;

        public SliderControl(String name, int min, int max, int initial, int step, String suffix) {
            this.setSpacing(6);

            this.suffix = suffix;
            nameLabel = new Label(name);
            nameLabel.setTextFill(Color.WHITE);
            nameLabel.setFont(Font.font(13));

            slider = new Slider(min, max, initial);
            slider.setBlockIncrement(step);
            slider.setMajorTickUnit(step);
            slider.setMinorTickCount(0);
            slider.setShowTickMarks(true);
            slider.setShowTickLabels(false);
            slider.setSnapToTicks(true);
            slider.setStyle("-fx-control-inner-background: #444444;");

            valueLabel = new Label(formatValue(initial));
            valueLabel.setTextFill(Color.WHITE);
            valueLabel.setFont(Font.font(13));

            slider.valueProperty().addListener((obs, oldVal, newVal) -> valueLabel.setText(formatValue(newVal.intValue())));

            valueLabel.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && event.getButton() == MouseButton.PRIMARY) {
                    TextField edit = new TextField(String.valueOf((int) slider.getValue()));
                    edit.setPrefWidth(50);
                    edit.setStyle("-fx-background-color: #2a2a2a; -fx-text-fill: white; -fx-border-color: #555555;");
                    this.getChildren().set(this.getChildren().indexOf(valueLabel), edit);
                    edit.requestFocus();

                    edit.setOnAction(e -> finishEdit(edit));
                    edit.focusedProperty().addListener((obs, wasFocused, isFocused) -> {
                        if (!isFocused) finishEdit(edit);
                    });
                }
            });

            this.getChildren().addAll(nameLabel, slider, valueLabel);
        }

        private void finishEdit(TextField edit) {
            int val;
            try {
                val = Integer.parseInt(edit.getText());
            } catch (NumberFormatException e) {
                val = (int) slider.getValue(); // fallback
            }
            int step = (int) slider.getBlockIncrement();
            val = ((val - (int) slider.getMin() + step / 2) / step) * step + (int) slider.getMin();
            val = Math.min(Math.max(val, (int) slider.getMin()), (int) slider.getMax());

            slider.setValue(val);
            this.getChildren().set(this.getChildren().indexOf(edit), valueLabel);
        }

        private String formatValue(int val) {
            return suffix.equals("%") ? val + "%" : val + " " + suffix;
        }
    }

    // --------------------- DoubleSliderControl ---------------------
    public static class DoubleSliderControl extends HBox {
        private final Label nameLabel;
        private final Slider lowSlider;
        private final Slider highSlider;
        private final Label valueLabel;
        private final String suffix;

        public DoubleSliderControl(String name, int min, int max, int low, int high, int step, String suffix) {
            this.setSpacing(6);

            this.suffix = suffix;
            nameLabel = new Label(name);
            nameLabel.setTextFill(Color.WHITE);
            nameLabel.setFont(Font.font(13));

            lowSlider = new Slider(min, max, low);
            highSlider = new Slider(min, max, high);
            lowSlider.setBlockIncrement(step);
            highSlider.setBlockIncrement(step);
            lowSlider.setSnapToTicks(true);
            highSlider.setSnapToTicks(true);
            lowSlider.setMajorTickUnit(step);
            highSlider.setMajorTickUnit(step);

            valueLabel = new Label(formatValue(low, high));
            valueLabel.setTextFill(Color.WHITE);
            valueLabel.setFont(Font.font(13));

            lowSlider.valueProperty().addListener((obs, oldVal, newVal) -> updateLabel());
            highSlider.valueProperty().addListener((obs, oldVal, newVal) -> updateLabel());

            valueLabel.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && event.getButton() == MouseButton.PRIMARY) {
                    TextField edit = new TextField(low + "-" + high);
                    edit.setPrefWidth(80);
                    edit.setStyle("-fx-background-color: #2a2a2a; -fx-text-fill: white; -fx-border-color: #555555;");
                    this.getChildren().set(this.getChildren().indexOf(valueLabel), edit);
                    edit.requestFocus();

                    edit.setOnAction(e -> finishEdit(edit));
                    edit.focusedProperty().addListener((obs, wasFocused, isFocused) -> {
                        if (!isFocused) finishEdit(edit);
                    });
                }
            });

            this.getChildren().addAll(nameLabel, lowSlider, highSlider, valueLabel);
        }

        private void updateLabel() {
            int low = (int) lowSlider.getValue();
            int high = (int) highSlider.getValue();
            if (low > high) { int tmp = low; low = high; high = tmp; }
            valueLabel.setText(formatValue(low, high));
        }

        private void finishEdit(TextField edit) {
            String[] parts = edit.getText().split("-");
            int low, high;
            try {
                low = Integer.parseInt(parts[0].trim());
                high = Integer.parseInt(parts[1].trim());
            } catch (Exception e) {
                low = (int) lowSlider.getValue();
                high = (int) highSlider.getValue();
            }

            int step = (int) lowSlider.getBlockIncrement();
            low = ((low - (int) lowSlider.getMin() + step / 2) / step) * step + (int) lowSlider.getMin();
            high = ((high - (int) highSlider.getMin() + step / 2) / step) * step + (int) highSlider.getMin();

            low = Math.min(Math.max(low, (int) lowSlider.getMin()), (int) lowSlider.getMax());
            high = Math.min(Math.max(high, (int) highSlider.getMin()), (int) highSlider.getMax());

            lowSlider.setValue(low);
            highSlider.setValue(high);
            this.getChildren().set(this.getChildren().indexOf(edit), valueLabel);
        }

        private String formatValue(int low, int high) {
            return suffix.equals("%") ? low + "-" + high + "%" : low + "-" + high + " " + suffix;
        }
    }
}