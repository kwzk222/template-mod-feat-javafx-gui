package name.modid.ui;

import javafx.application.Platform;

public class JavaFXInitializer {
    private static boolean started = false;

    public static void ensureInit() {
        if (!started) {
            try {
                Platform.startup(() -> {}); // initialize JavaFX
            } catch (IllegalStateException ignored) {
                // toolkit already started
            }
            started = true;
        }
    }
}