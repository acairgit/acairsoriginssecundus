package com.acair.acairsoriginssecundus.client.screen;

import com.acair.acairsoriginssecundus.character.Race;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.Component;

import java.util.Arrays;
import java.util.List;

/**
 * Экран выбора расы. Слева расположен список всех рас,
 * позволяющий быстро пролистывать варианты. В центре
 * отображается модель игрока, а справа блок с названием
 * и описанием выбранной расы.
 */
public class RaceSelectScreen extends Screen {
    private final List<Race> races = Arrays.asList(Race.values());
    private RaceList raceList;
    private Button done;

    public RaceSelectScreen() {
        super(Component.translatable("screen.acairsoriginssecundus.select_race"));
    }

    @Override
    protected void init() {
        int listWidth = 80;
        // Создаём полоску прокрутки со списком рас
        this.raceList = new RaceList(this.minecraft, 20, 20, listWidth, this.height - 40, races);
        this.addRenderableWidget(this.raceList);

        // Кнопка подтверждения выбора находится снизу по центру
        this.done = this.addRenderableWidget(Button.builder(Component.translatable("gui.done"), b -> confirm())
                .bounds(this.width / 2 - 50, this.height - 30, 100, 20).build());
    }

    private void confirm() {
        // Переходим к экрану редактора с выбранной расой
        this.minecraft.setScreen(new CharacterEditorScreen(this.raceList.getSelectedRace()));
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        // Фон как при загрузке мира
        this.renderDirtBackground(graphics);

        Race current = this.raceList.getSelectedRace();
        int modelX = this.width / 2 - 30; // модель ближе к центру
        int modelY = this.height / 2 + 40;

        // Немного увеличиваем размер модели
        InventoryScreen.renderEntityInInventoryFollowsMouse(graphics, modelX, modelY, 50,
                modelX - mouseX, modelY - 50 - mouseY, Minecraft.getInstance().player);

        // Блок с описанием справа
        int infoLeft = this.width / 2 + 40;
        graphics.drawString(this.font, current.getName(), infoLeft, 40, 0xFFFFFF, false);
        graphics.drawWordWrap(this.font, current.getDescription(), infoLeft, 60, 120, 0xFFFFFF);

        super.render(graphics, mouseX, mouseY, partialTick);
    }
}
