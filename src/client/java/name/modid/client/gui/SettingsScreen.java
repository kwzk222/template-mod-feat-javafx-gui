package name.modid.client.gui;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class SettingsScreen extends Screen {
    private final Screen parent;
    private final List<ClickableWidget> widgets = new ArrayList<>();
    private boolean moduleExpanded = false;
    private boolean moduleEnabled = false;

    public static KeyBinding openKey;

    public SettingsScreen(Screen parent) {
        super(Text.literal("Settings"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        widgets.clear();

        // Module header
        widgets.add(new ModuleHeaderWidget(width / 2 - 100, 40, 200, 20,
                () -> moduleEnabled, (val) -> moduleEnabled = val,
                () -> moduleExpanded, (val) -> moduleExpanded = val));

        // Pill toggle example
        widgets.add(new PillToggleWidget(width / 2 - 80, 70, 60, 20,
                () -> true, (val) -> {}, "Auto Attack"));

        // Slider example
        widgets.add(new RedSliderWidget(width / 2 - 80, 100, 120, 20, 0, 100, 50));

        // Dropdown selector example
        widgets.add(new DropdownWidget(width / 2 - 80, 130, 120, 20,
                new String[]{"Option A", "Option B", "Option C"}));
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        renderBackground(context);
        for (ClickableWidget w : widgets) {
            if (w instanceof ModuleHeaderWidget || moduleExpanded) {
                w.render(context, mouseX, mouseY, delta);
            }
        }
        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    // ------------------ Custom Widgets ------------------ //

    static class PillToggleWidget extends ClickableWidget {
        private final Supplier<Boolean> getter;
        private final Consumer<Boolean> setter;
        private final String label;

        public PillToggleWidget(int x, int y, int width, int height,
                                Supplier<Boolean> getter, Consumer<Boolean> setter, String label) {
            super(x, y, width, height, Text.literal(""));
            this.getter = getter;
            this.setter = setter;
            this.label = label;
        }

        @Override
        protected void renderButton(DrawContext context, int mouseX, int mouseY, float delta) {
            boolean state = getter.get();
            int color = state ? 0xAAFF0000 : 0xAA333333;
            context.fill(x, y, x + width, y + height, color);
            context.drawCenteredTextWithShadow(textRenderer, label + ": " + (state ? "ON" : "OFF"),
                    x + width / 2, y + (height - 8) / 2, 0xFFFFFF);
        }

        @Override
        public void onClick(double mouseX, double mouseY) {
            setter.accept(!getter.get());
        }
    }

    static class ModuleHeaderWidget extends ClickableWidget {
        private final Supplier<Boolean> enabledGetter;
        private final Consumer<Boolean> enabledSetter;
        private final Supplier<Boolean> expandedGetter;
        private final Consumer<Boolean> expandedSetter;

        public ModuleHeaderWidget(int x, int y, int width, int height,
                                  Supplier<Boolean> enabledGetter, Consumer<Boolean> enabledSetter,
                                  Supplier<Boolean> expandedGetter, Consumer<Boolean> expandedSetter) {
            super(x, y, width, height, Text.literal("Module"));
            this.enabledGetter = enabledGetter;
            this.enabledSetter = enabledSetter;
            this.expandedGetter = expandedGetter;
            this.expandedSetter = expandedSetter;
        }

        @Override
        protected void renderButton(DrawContext context, int mouseX, int mouseY, float delta) {
            boolean enabled = enabledGetter.get();
            boolean expanded = expandedGetter.get();

            context.fill(x, y, x + width, y + height, 0xAA111111);
            context.drawTextWithShadow(textRenderer, "Combat Module", x + 5, y + 6, 0xFFFFFF);

            int toggleColor = enabled ? 0xAAFF0000 : 0xAA333333;
            context.fill(x + width - 60, y + 2, x + width - 30, y + height - 2, toggleColor);
            context.drawCenteredTextWithShadow(textRenderer, enabled ? "ON" : "OFF",
                    x + width - 45, y + 6, 0xFFFFFF);

            context.drawTextWithShadow(textRenderer, expanded ? "▲" : "▼", x + width - 15, y + 6, 0xFFFFFF);
        }

        @Override
        public void onClick(double mouseX, double mouseY) {
            int localX = (int) mouseX - x;
            if (localX > width - 60 && localX < width - 30) {
                enabledSetter.accept(!enabledGetter.get());
            } else if (localX > width - 20) {
                expandedSetter.accept(!expandedGetter.get());
            }
        }
    }

    static class RedSliderWidget extends SliderWidget {
        private final int min;
        private final int max;

        public RedSliderWidget(int x, int y, int width, int height, int min, int max, int value) {
            super(x, y, width, height, Text.literal(""), (double) (value - min) / (max - min));
            this.min = min;
            this.max = max;
        }

        @Override
        protected void updateMessage() {
            setMessage(Text.literal("" + getValue()));
        }

        @Override
        protected void applyValue() {}

        public int getValue() {
            return (int) (value * (max - min) + min);
        }

        @Override
        public void renderButton(DrawContext context, int mouseX, int mouseY, float delta) {
            int filledWidth = (int)(value * width);
            context.fill(x, y + height / 2 - 2, x + filledWidth, y + height / 2 + 2, 0xAAFF0000);
            int thumbX = x + filledWidth - 4;
            context.fill(thumbX, y, thumbX + 8, y + height, 0xFFFFFFFF);
            context.drawCenteredTextWithShadow(textRenderer, getMessage(), x + width / 2, y - 10, 0xFFFFFF);
        }
    }

    static class DropdownWidget extends ClickableWidget {
        private final String[] options;
        private int selected = 0;
        private boolean open = false;

        public DropdownWidget(int x, int y, int width, int height, String[] options) {
            super(x, y, width, height, Text.literal(""));
            this.options = options;
        }

        @Override
        protected void renderButton(DrawContext context, int mouseX, int mouseY, float delta) {
            context.fill(x, y, x + width, y + height, 0xAA333333);
            context.drawCenteredTextWithShadow(textRenderer, options[selected], x + width / 2, y + 6, 0xFFFFFF);

            if (open) {
                for (int i = 0; i < options.length; i++) {
                    int oy = y + height + i * height;
                    context.fill(x, oy, x + width, oy + height, 0xAA111111);
                    context.drawCenteredTextWithShadow(textRenderer, options[i], x + width / 2, oy + 6, 0xFFFFFF);
                }
            }
        }

        @Override
        public void onClick(double mouseX, double mouseY) {
            if (open) {
                int index = (int) ((mouseY - (y + height)) / height);
                if (index >= 0 && index < options.length) selected = index;
            }
            open = !open;
        }
    }

    // ------------------ Keybinding ------------------ //

    public static void registerKeybind() {
        openKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.templatemod.opensettings",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_RIGHT_SHIFT,
                "key.categories.misc"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (openKey.wasPressed()) client.setScreen(new SettingsScreen(null));
        });
    }
}