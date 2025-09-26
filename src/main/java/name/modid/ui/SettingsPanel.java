package name.modid.ui;

import javafx.geometry.Insets;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.VBox;

public class SettingsPanel extends VBox {

    public SettingsPanel() {
        // Configure the VBox itself
        setSpacing(12);
        setPadding(new Insets(12));
        setStyle("-fx-background-color: " + UIConstants.BG + ";");

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

        // Add all modules to this VBox
        getChildren().addAll(combatModule, movementModule);
    }
}