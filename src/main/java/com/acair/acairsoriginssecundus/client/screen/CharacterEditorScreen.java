package com.acair.acairsoriginssecundus.client.screen;

import com.acair.acairsoriginssecundus.character.EarType;
import com.acair.acairsoriginssecundus.character.EyeColor;
import com.acair.acairsoriginssecundus.character.Race;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.Component;

import java.util.Arrays;
import java.util.List;

/**
 * Экран редактора персонажа. Отличается от экрана выбора
 * рас только тем, что справа вместо описания располагаются
 * элементы настройки модели.
 */
public class CharacterEditorScreen extends Screen {
    private final List<Race> races = Arrays.asList(Race.values());
    private final Race initialRace;
    private RaceList raceList;

    private AbstractSliderButton heightSlider;
    private CycleButton<EyeColor> eyeColor;
    private CycleButton<EarType> earType;
    private Button back;
    private Button done;

    public CharacterEditorScreen(Race race) {
        super(Component.translatable("screen.acairsoriginssecundus.editor"));
        this.initialRace = race;
    }

    @Override
    protected void init() {
        int listWidth = 80;
        // Полоска прокрутки рас располагается слева, как и на предыдущем экране
        this.raceList = new RaceList(this.minecraft, 20, 20, listWidth, this.height - 40, races);
        this.raceList.setSelectedRace(this.initialRace);
        this.addRenderableWidget(this.raceList);

        int editorLeft = this.width / 2 + 40;
        int editorTop = this.height / 2 - 30;

        // Слайдер высоты персонажа
        this.heightSlider = new AbstractSliderButton(editorLeft, editorTop, 120, 20,
                Component.translatable("option.acairsoriginssecundus.height"), 0.5) {
            @Override
            protected void updateMessage() {
                setMessage(Component.translatable("option.acairsoriginssecundus.height", String.format("%.2f", value)));
            }

            @Override
            protected void applyValue() {
                // Здесь сохраняем выбранное значение при необходимости
            }
        };
        this.addRenderableWidget(this.heightSlider);

        // Выбор цвета глаз
        this.eyeColor = this.addRenderableWidget(
                CycleButton.builder(EyeColor::getName)
                        .withValues(EyeColor.values())
                        .create(editorLeft, editorTop + 25, 120, 20,
                                Component.translatable("option.acairsoriginssecundus.eye_color")));

        if (this.initialRace == Race.ELF) {
            // У эльфов доступен выбор типа ушей
            this.earType = this.addRenderableWidget(
                    CycleButton.builder(EarType::getName)
                            .withValues(EarType.values())
                            .create(editorLeft, editorTop + 50, 120, 20,
                                    Component.translatable("option.acairsoriginssecundus.ear_type")));
        }

        // Кнопки "Назад" и "Готово"
        this.back = this.addRenderableWidget(Button.builder(Component.translatable("gui.back"),
                b -> this.minecraft.setScreen(new RaceSelectScreen()))
                .bounds(editorLeft, this.height - 30, 50, 20).build());
        this.done = this.addRenderableWidget(Button.builder(Component.translatable("gui.done"),
                b -> this.minecraft.setScreen(null))
                .bounds(editorLeft + 70, this.height - 30, 50, 20).build());
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        this.renderDirtBackground(graphics);

        // Модель игрока по центру и немного увеличенного размера
        int modelX = this.width / 2 - 30;
        int modelY = this.height / 2 + 40;
        InventoryScreen.renderEntityInInventoryFollowsMouse(graphics, modelX, modelY, 50,
                modelX - mouseX, modelY - 50 - mouseY, Minecraft.getInstance().player);

        super.render(graphics, mouseX, mouseY, partialTick);
    }
}
