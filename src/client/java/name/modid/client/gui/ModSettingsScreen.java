package name.modid.client.gui;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;

import java.util.function.Consumer;

public class ModSettingsScreen extends Screen {

    // State for Auto Attack Module
    private boolean autoAttackActive = true;
    private boolean autoAttackOpen = false;
    private static final String[] AUTO_ATTACK_TARGET_OPTIONS = {"Player", "Mob", "All"};
    private int autoAttackTargetIndex = 0;
    private double autoAttackDelay = 10;
    private double autoAttackMinChance = 10;
    private double autoAttackMaxChance = 50;

    // State for Flight Module
    private boolean flightActive = false;
    private boolean flightOpen = false;
    private double flightSpeed = 2;

    public ModSettingsScreen(Text title) {
        super(title);
    }

    @Override
    public void init() {
        super.init();

        int y = 30;
        int x = this.width / 2 - 150;
        int widgetWidth = 300;

        // --- Auto Attack Module ---
        y = addModuleBar(x, y, widgetWidth, "Auto Attack", autoAttackActive, autoAttackOpen,
            (button) -> { // Toggle action
                autoAttackActive = !autoAttackActive;
                this.rebuildWidgets();
            },
            (button) -> { // Arrow action
                autoAttackOpen = !autoAttackOpen;
                this.rebuildWidgets();
            }
        );

        if (autoAttackOpen) {
            y = addDropdown(x, y, widgetWidth, "Target", AUTO_ATTACK_TARGET_OPTIONS, autoAttackTargetIndex, (newIndex) -> autoAttackTargetIndex = newIndex);
            y = addSlider(x, y, widgetWidth, "Delay", "ticks", 0, 50, autoAttackDelay, (newValue) -> autoAttackDelay = newValue);

            // Double Slider for Auto Attack Chance
            this.addDrawableChild(new TextWidget(x + 15, y, widgetWidth - 15, 10, Text.of("Chance")));
            y += 15;

            addDrawableChild(new SimpleSlider(x + 15, y, widgetWidth - 15, 20, Text.of("Min"), "%", 0, 100, autoAttackMinChance, (newLow) -> {
                autoAttackMinChance = Math.min(newLow, autoAttackMaxChance);
                this.rebuildWidgets();
            }));
            y += 25;

            addDrawableChild(new SimpleSlider(x + 15, y, widgetWidth - 15, 20, Text.of("Max"), "%", 0, 100, autoAttackMaxChance, (newHigh) -> {
                autoAttackMaxChance = Math.max(newHigh, autoAttackMinChance);
                this.rebuildWidgets();
            }));
            y += 25;
        }

        // --- Flight Module ---
        y = addModuleBar(x, y, widgetWidth, "Flight", flightActive, flightOpen,
            (button) -> {
                flightActive = !flightActive;
                this.rebuildWidgets();
            },
            (button) -> {
                flightOpen = !flightOpen;
                this.rebuildWidgets();
            }
        );

        if (flightOpen) {
            y = addSlider(x, y, widgetWidth, "Speed", "x", 0, 10, flightSpeed, (newValue) -> flightSpeed = newValue);
        }
    }

    private void rebuildWidgets() {
        this.clearChildren();
        this.init();
    }

    private int addModuleBar(int x, int y, int width, String name, boolean active, boolean open, ButtonWidget.PressAction toggleAction, ButtonWidget.PressAction arrowAction) {
        String status = active ? " [ON]" : " [OFF]";
        this.addDrawableChild(new ButtonWidget.Builder(Text.of(name + status), toggleAction)
                .dimensions(x, y, width - 25, 20)
                .build());

        this.addDrawableChild(new ButtonWidget.Builder(Text.of(open ? "▲" : "▼"), arrowAction)
                .dimensions(x + width - 25, y, 20, 20)
                .build());
        return y + 25;
    }

    private int addDropdown(int x, int y, int width, String name, String[] options, int currentIndex, Consumer<Integer> callback) {
        this.addDrawableChild(new ButtonWidget.Builder(Text.of(name + ": " + options[currentIndex]), (button) -> {
            int nextIndex = (currentIndex + 1) % options.length;
            callback.accept(nextIndex);
            this.rebuildWidgets();
        }).dimensions(x + 15, y, width - 15, 20).build());
        return y + 25;
    }

    private int addSlider(int x, int y, int width, String name, String unit, double min, double max, double currentValue, Consumer<Double> callback) {
        addDrawableChild(new SimpleSlider(x + 15, y, width - 15, 20, Text.of(name), unit, min, max, currentValue, callback));
        return y + 25;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        context.fill(0, 0, this.width, this.height, 0xFF330000);
        context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, 15, 0xFFFFFFFF);
        super.render(context, mouseX, mouseY, delta);
    }

    private class TextWidget extends ClickableWidget {
        public TextWidget(int x, int y, int width, int height, Text message) {
            super(x, y, width, height, message);
        }

        @Override
        public void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
            context.drawTextWithShadow(ModSettingsScreen.this.textRenderer, this.getMessage(), this.getX(), this.getY() + (this.height - 8) / 2, 0xFFFFFFFF);
        }

        @Override
        public void onClick(double mouseX, double mouseY) {
            // Not clickable
        }

        @Override
        protected void appendClickableNarrations(NarrationMessageBuilder builder) {
            this.appendDefaultNarrations(builder);
        }
    }

    private class SimpleSlider extends SliderWidget {
        private final String unit;
        private final double min;
        private final double max;
        private final Consumer<Double> callback;
        private final Text baseMessage;

        public SimpleSlider(int x, int y, int width, int height, Text message, String unit, double min, double max, double currentValue, Consumer<Double> callback) {
            super(x, y, width, height, message, 0);
            this.baseMessage = message;
            this.unit = unit;
            this.min = min;
            this.max = max;
            this.callback = callback;
            this.value = (MathHelper.clamp(currentValue, min, max) - min) / (max - min);
            updateMessage();
        }

        @Override
        protected void updateMessage() {
            double actualValue = (this.value * (max - min)) + min;
            this.setMessage(Text.of(baseMessage.getString() + ": " + String.format("%.2f", actualValue) + " " + this.unit));
        }

        @Override
        protected void applyValue() {
            double actualValue = (this.value * (max - min)) + min;
            callback.accept(actualValue);
        }
    }
}