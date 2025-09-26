package name.modid;

import javafx.application.Platform;

public class JavaFXInitializer {
    private static boolean initialized = false;

    public static void init() {
        if (!initialized) {
            // Startup JavaFX toolkit once
            Platform.startup(() -> {});
            initialized = true;
        }
    }
}