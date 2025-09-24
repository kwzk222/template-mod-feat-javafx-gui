package name.modid;

import name.modid.ui.SettingsPanel;
import name.modid.ui.components.DoubleSlider;
import name.modid.ui.components.SingleSlider;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.util.Window;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.lwjgl.glfw.GLFW;

import java.util.Arrays;

public class TemplateModClient implements ClientModInitializer {
    private static volatile Stage overlayStage;
    private static SettingsPanel settingsPanel;

    private static int lastWinX = Integer.MIN_VALUE;
    private static int lastWinY = Integer.MIN_VALUE;
    private static int lastWinW = Integer.MIN_VALUE;
    private static int lastWinH = Integer.MIN_VALUE;

    private static KeyBinding toggleGuiKeyBinding;

    @Override
    public void onInitializeClient() {
        toggleGuiKeyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.template-mod.toggle_gui",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_RIGHT_SHIFT,
                "category.template-mod.main"
        ));

        new JFXPanel(); // Initializes the JavaFX toolkit

        Platform.runLater(() -> {
            overlayStage = new Stage();
            overlayStage.initStyle(StageStyle.TRANSPARENT);
            overlayStage.setAlwaysOnTop(true);

            settingsPanel = new SettingsPanel("Template Mod Settings");
            settingsPanel.getStyleClass().add("settings-root");


            // Toggle
            ToggleButton toggle = new ToggleButton("ON");
            toggle.setSelected(true);
            settingsPanel.addSetting(makeToggleRow("Enabled", toggle));

            // Single slider (Chance)
            SingleSlider chanceSlider = new SingleSlider(0, 100, 50, 1);
            settingsPanel.addSetting(makeSingleSliderRow("Chance", chanceSlider));

            // Double slider (Delay)
            DoubleSlider delaySlider = new DoubleSlider(0, 50, 7, 21, 1);
            settingsPanel.addSetting(makeDoubleSliderRow("Delay", delaySlider));

            // Select box
            ComboBox<String> combo = new ComboBox<>();
            combo.getItems().addAll("Alpha", "Beta", "Gamma");
            combo.setValue("Alpha");
            settingsPanel.addSetting(makeSelectRow("Mode", combo));

            Group root = new Group(settingsPanel);
            Scene scene = new Scene(root);
            scene.setFill(Color.TRANSPARENT);
            scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
            overlayStage.setScene(scene);
            overlayStage.show();
        });

        ClientTickEvents.START_CLIENT_TICK.register(client -> {
            if (client == null) return;

            while (toggleGuiKeyBinding.wasPressed()) {
                if (overlayStage != null) {
                    Platform.runLater(() -> {
                        if (overlayStage.isShowing()) {
                            overlayStage.hide();
                        } else {
                            overlayStage.show();
                        }
                    });
                }
            }

            if (overlayStage == null || !overlayStage.isShowing()) return;

            Window w = client.getWindow();
            if (w == null) return;

            int winX = w.getX();
            int winY = w.getY();
            int winW = w.getWidth();
            int winH = w.getHeight();

            if (winX != lastWinX || winY != lastWinY || winW != lastWinW || winH != lastWinH) {
                lastWinX = winX;
                lastWinY = winY;
                lastWinW = winW;
                lastWinH = winH;
                Platform.runLater(() -> {
                    if (overlayStage != null) {
                        overlayStage.setX(winX + 40);
                        overlayStage.setY(winY + 40);
                    }
                });
            }
        });
    }

    private HBox makeToggleRow(String name, ToggleButton toggle) {
        Label label = new Label(name);
        label.setTextFill(Color.WHITE);
        label.setPrefWidth(80);

        toggle.setMinWidth(60);
        toggle.setPrefWidth(60);
        toggle.setMaxWidth(60);
        toggle.setAlignment(Pos.CENTER);
        toggle.setStyle("-fx-background-color: #e53935; -fx-text-fill: white;");
        toggle.setCursor(Cursor.HAND);

        toggle.selectedProperty().addListener((obs, oldVal, isOn) -> {
            if (isOn) {
                toggle.setText("ON");
                toggle.setStyle("-fx-background-color: #43a047; -fx-text-fill: white;");
            } else {
                toggle.setText("OFF");
                toggle.setStyle("-fx-background-color: #e53935; -fx-text-fill: white;");
            }
        });

        HBox row = new HBox(10, label, toggle);
        row.setAlignment(Pos.CENTER_LEFT);
        return row;
    }

    private HBox makeSingleSliderRow(String name, SingleSlider slider) {
        Label label = new Label(name);
        label.setTextFill(Color.WHITE);
        label.setPrefWidth(80);
        Label valueLabel = new Label((int) slider.getValue() + "%");
        valueLabel.setTextFill(Color.WHITE);
        slider.setOnValueChanged(val -> valueLabel.setText((int) val.doubleValue() + "%"));
        HBox row = new HBox(10, label, slider, valueLabel);
        row.setAlignment(Pos.CENTER_LEFT);
        return row;
    }

    private HBox makeDoubleSliderRow(String name, DoubleSlider slider) {
        Label label = new Label(name);
        label.setTextFill(Color.WHITE);
        label.setPrefWidth(80);
        Label valueLabel = new Label((int)slider.getLow() + "-" + (int)slider.getHigh() + " ticks");
        valueLabel.setTextFill(Color.WHITE);
        slider.addChangeListener(() -> valueLabel.setText((int)slider.getLow() + "-" + (int)slider.getHigh() + " ticks"));
        HBox row = new HBox(10, label, slider, valueLabel);
        row.setAlignment(Pos.CENTER_LEFT);
        return row;
    }

    private HBox makeSelectRow(String name, ComboBox<String> combo) {
        Label label = new Label(name);
        label.setTextFill(Color.WHITE);
        label.setPrefWidth(80);

        combo.setCursor(Cursor.HAND);

        combo.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item);
                setTextFill(Color.WHITE);
                setStyle("-fx-background-color: #333; -fx-border-width: 0;");
            }
        });
        combo.setCellFactory(list -> new ListCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item);
                setTextFill(Color.WHITE);
                setStyle("-fx-background-color: #222; -fx-border-width: 0;");
            }
        });

        HBox row = new HBox(10, label, combo);
        row.setAlignment(Pos.CENTER_LEFT);
        return row;
    }
}