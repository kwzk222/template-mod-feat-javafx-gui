package name.modid;

import name.modid.client.gui.SettingsScreen;
import net.fabricmc.api.ClientModInitializer;

public class TemplateModClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        SettingsScreen.registerKeybind();
    }
}