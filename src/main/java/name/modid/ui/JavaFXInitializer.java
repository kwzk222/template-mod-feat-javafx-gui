package name.modid.ui;

import javafx.application.Platform;

public class JavaFXInitializer {
    private static boolean started = false;

    /**
     * Call this once before any JavaFX UI is created.
     */
    public static void ensureInitialized() {
        if (!started) {
            try {
                // This starts the JavaFX toolkit
                Platform.startup(() -> {});
            } catch (IllegalStateException ignored) {
                // Toolkit already started
            }
            started = true;
        }
    }
}