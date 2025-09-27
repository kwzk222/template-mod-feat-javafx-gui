package name.modid;

import javafx.application.Platform;
import java.util.concurrent.CountDownLatch;

public class JavaFXInitializer {
    private static boolean started = false;

    public static synchronized void init() {
        if (!started) {
            CountDownLatch latch = new CountDownLatch(1);
            new Thread(() -> {
                Platform.startup(() -> {
                    // no-op, just initializes JavaFX
                    latch.countDown();
                });
            }, "JavaFX-Init-Thread").start();

            try {
                latch.await(); // wait until JavaFX is ready
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            started = true;
        }
    }
}