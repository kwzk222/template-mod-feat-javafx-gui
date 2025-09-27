package name.modid;

import name.modid.client.gui.ModSettingsScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

public class ModClientInitializer implements ClientModInitializer {

    private boolean opened = false;

    @Override
    public void onInitializeClient() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (!opened) {
                MinecraftClient.getInstance().setScreen(new ModSettingsScreen(Text.of("Mod Settings")));
                opened = true;
            }
        });
    }
}