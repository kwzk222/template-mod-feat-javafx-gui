package name.modid.ui;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class SettingsPanel extends Stage {

    public SettingsPanel() {
        setTitle("Template Mod Settings");

        VBox root = new VBox(12);
        root.setPadding(new Insets(12));
        root.setStyle("-fx-background-color: " + UIConstants.BG + ";");

        // --- Combat module ---
        ModuleSection combatModule = new ModuleSection("Combat");
        combatModule.setEnabled(true); // Example: start with this module enabled

        // AutoAttack Toggle (uses the new TogglePill)
        combatModule.addSetting(SettingRows.makeRow("AutoAttack", new TogglePill(true)));

        // Chance Slider
        combatModule.addSetting(SettingRows.makeRow("Chance", new SingleSliderControl(0, 100, 50, 1)));

        // Delay Double Slider
        combatModule.addSetting(SettingRows.makeRow("Delay", new DoubleSliderControl(0, 50, 7, 21, 1)));

        // --- Movement module ---
        ModuleSection movementModule = new ModuleSection("Movement");

        // Mode Dropdown
        ComboBox<String> modeDropdown = new ComboBox<>();
        modeDropdown.getItems().addAll("Alpha", "Beta", "Gamma");
        modeDropdown.setValue("Alpha");
        movementModule.addSetting(SettingRows.makeSelectRow("Mode", modeDropdown));

        // Speed Slider
        movementModule.addSetting(SettingRows.makeRow("Speed", new SingleSliderControl(0, 10, 5, 1)));

        // Add all modules to root
        root.getChildren().addAll(combatModule, movementModule);

        // Wrap root in a ScrollPane for scrollability
        ScrollPane scrollPane = new ScrollPane(root);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: " + UIConstants.BG + "; -fx-border-color: " + UIConstants.BORDER + ";");

        Scene scene = new Scene(scrollPane, 420, 500); // Increased width slightly for better spacing

        // Load the external stylesheet
        try {
            String css = getClass().getResource("/style.css").toExternalForm();
            if (css != null) {
                scene.getStylesheets().add(css);
            }
        } catch (Exception e) {
            System.err.println("Error loading stylesheet: " + e.getMessage());
        }

        setScene(scene);
    }
}