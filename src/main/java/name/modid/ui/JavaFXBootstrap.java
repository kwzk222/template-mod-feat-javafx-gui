package name.modid.ui;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;

/**
 * Initializes JavaFX runtime inside a Minecraft + Fabric environment.
 */
public class JavaFXBootstrap {

    private static boolean started = false;

    public static void init() {
        if (!started) {
            // JFXPanel will spin up the JavaFX toolkit without creating a Stage
            new JFXPanel();
            started = true;
        }
    }

    public static void runLater(Runnable task) {
        init();
        Platform.runLater(task);
    }
}