package name.modid.ui;

import javafx.geometry.Insets;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import name.modid.ui.DoubleSliderControl;
import name.modid.ui.SingleSliderControl;
import name.modid.ui.TogglePill;

public class SettingsPanel extends ScrollPane {

    public SettingsPanel() {
        // Configure the ScrollPane itself
        setFitToWidth(true);
        setStyle("-fx-background: " + UIConstants.BG + "; -fx-border-color: " + UIConstants.BORDER + ";");

        // VBox to hold the modules, which will be the content of the ScrollPane
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

        // Set the VBox as the content of this ScrollPane
        setContent(root);
    }
}