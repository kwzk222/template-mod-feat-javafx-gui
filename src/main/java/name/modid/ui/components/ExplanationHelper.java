package name.modid.ui.components;

import javafx.scene.Node;
import javafx.scene.control.Tooltip;

public class ExplanationHelper {
    public static void attachExplanation(Node target, String explanation) {
        Tooltip tip = new Tooltip(explanation);
        Tooltip.install(target, tip);
    }
}
