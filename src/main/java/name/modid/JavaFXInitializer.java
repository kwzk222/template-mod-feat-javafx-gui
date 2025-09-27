package name.modid;

import javafx.application.Platform;

public class JavaFXInitializer {
    private static boolean started = false;

    public static synchronized void init() {
        if (!started) {
            try {
                // This starts JavaFX in the background
                new Thread(() -> Platform.startup(() -> {
                    // no-op
                }), "JavaFX-Init-Thread").start();
            } catch (IllegalStateException e) {
                // already started
            }
            started = true;
        }
    }
}