package com.acair.acairsoriginssecundus.client.screen;

import com.acair.acairsoriginssecundus.character.Race;
import com.acair.acairsoriginssecundus.character.EyeColor;
import com.acair.acairsoriginssecundus.character.EarType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

/**
 * Экран редактора персонажа. Здесь размещены простые
 * элементы управления для изменения параметров.
 */
public class CharacterEditorScreen extends Screen {
    private final Race race;
    private AbstractSliderButton heightSlider;
    // Кнопка для выбора цвета глаз
    private CycleButton<EyeColor> eyeColor;
    // Кнопка для выбора типа ушей, только для некоторых рас
    private CycleButton<EarType> earType;
    private Button back;
    private Button done;

    public CharacterEditorScreen(Race race) {
        super(Component.translatable("screen.acairsoriginssecundus.editor"));
        this.race = race;
    }

    @Override
    protected void init() {
        int centerX = this.width / 2;
        int centerY = this.height / 2;
        // Простой слайдер для роста персонажа
        this.heightSlider = new AbstractSliderButton(centerX - 100, centerY - 20, 200, 20, Component.translatable("option.acairsoriginssecundus.height"), 0.5) {
            @Override
            protected void updateMessage() {
                setMessage(Component.translatable("option.acairsoriginssecundus.height", String.format("%.2f", this.value)));
            }

            @Override
            protected void applyValue() {
                // Здесь можно сохранить значение в конфигурацию игрока
            }
        };
        this.addRenderableWidget(this.heightSlider);

        // Переключатель цвета глаз
        this.eyeColor = this.addRenderableWidget(
                CycleButton.builder(EyeColor::getName)
                        .withValues(EyeColor.values())
                        .create(centerX - 100, centerY + 10, 200, 20,
                                Component.translatable("option.acairsoriginssecundus.eye_color")));

        // Если раса поддерживает выбор типа ушей, добавляем соответствующий переключатель
        if (this.race == Race.ELF) {
            this.earType = this.addRenderableWidget(
                    CycleButton.builder(EarType::getName)
                            .withValues(EarType.values())
                            .create(centerX - 100, centerY + 40, 200, 20,
                                    Component.translatable("option.acairsoriginssecundus.ear_type")));
        }

        // Кнопки навигации
        this.back = this.addRenderableWidget(Button.builder(Component.translatable("gui.back"),
                b -> this.minecraft.setScreen(new RaceSelectScreen()))
                .bounds(centerX - 100, centerY + 70, 80, 20).build());
        this.done = this.addRenderableWidget(Button.builder(Component.translatable("gui.done"),
                b -> this.minecraft.setScreen(null))
                .bounds(centerX + 20, centerY + 70, 80, 20).build());
        // Кнопки навигации
        this.back = this.addRenderableWidget(Button.builder(Component.translatable("gui.back"), b -> this.minecraft.setScreen(new RaceSelectScreen()))
                .bounds(centerX - 100, centerY + 40, 80, 20).build());
        this.done = this.addRenderableWidget(Button.builder(Component.translatable("gui.done"), b -> this.minecraft.setScreen(null))
                .bounds(centerX + 20, centerY + 40, 80, 20).build());
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(graphics);
        int centerX = this.width / 2;
        int centerY = this.height / 2;
        // Заголовок с выбранной расой
        graphics.drawCenteredString(this.font, this.race.getName(), centerX, centerY - 60, 0xFFFFFF);
        super.render(graphics, mouseX, mouseY, partialTick);
    }
}
