package name.modid.ui;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import name.modid.ui.components.ToggleSwitch;
import name.modid.ui.components.DoubleSlider;
import name.modid.ui.components.SingleSlider;

public class SettingsPanel extends Stage {

    public SettingsPanel() {
        setTitle("Template Mod Settings");

        VBox root = new VBox(12);
        root.setPadding(new Insets(12));
        root.setStyle("-fx-background-color: #2b2b2b;");

        // --- Combat module ---
        ModuleSection combatModule = new ModuleSection("Combat");

        // AutoAttack Toggle
        combatModule.addSetting(new ToggleSwitch("AutoAttack"));

        // Chance Slider
        SingleSlider chanceSlider = new SingleSlider(0, 100, 50, 1);
        combatModule.addSetting(SettingRows.makeSingleSliderRow("Chance", chanceSlider));

        // Delay Double Slider
        DoubleSlider delaySlider = new DoubleSlider(0, 50, 7, 21, 1);
        combatModule.addSetting(SettingRows.makeDoubleSliderRow("Delay", delaySlider));

        // --- Movement module ---
        ModuleSection movementModule = new ModuleSection("Movement");

        // Mode Dropdown
        ComboBox<String> modeDropdown = new ComboBox<>();
        modeDropdown.getItems().addAll("Alpha", "Beta", "Gamma");
        modeDropdown.setValue("Alpha");
        movementModule.addSetting(SettingRows.makeSelectRow("Mode", modeDropdown));

        // Speed Slider
        SingleSlider speedSlider = new SingleSlider(0, 10, 5, 1);
        movementModule.addSetting(SettingRows.makeSingleSliderRow("Speed", speedSlider));

        // Add all modules to root
        root.getChildren().addAll(combatModule, movementModule);

        // Wrap root in a ScrollPane for scrollability
        ScrollPane scrollPane = new ScrollPane(root);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: #2b2b2b; -fx-border-color: #444444;");

        Scene scene = new Scene(scrollPane, 400, 500);

        // Load the external stylesheet
        try {
            String css = getClass().getResource("/style.css").toExternalForm();
            scene.getStylesheets().add(css);
        } catch (Exception e) {
            System.err.println("Error loading stylesheet: " + e.getMessage());
        }

        setScene(scene);
    }
}