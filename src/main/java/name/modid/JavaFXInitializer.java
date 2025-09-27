package name.modid;

import javafx.application.Platform;

public class JavaFXInitializer {
    private static boolean initialized = false;

    public static void init() {
        if (!initialized) {
            initialized = true;
            new Thread(() -> {
                Platform.startup(() -> {
                    // Toolkit initialized, but do nothing yet
                });
            }, "JavaFX Init Thread").start();
        }
    }
}