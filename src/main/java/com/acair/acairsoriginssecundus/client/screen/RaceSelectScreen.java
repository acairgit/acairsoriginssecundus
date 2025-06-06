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
 * Экран выбора расы. Слева отображается модель игрока,
 * справа описание выбранной расы. Стрелками можно
 * переключать расы, а кнопка "Готово" подтверждает выбор
 * и открывает экран редактора персонажа.
 */
public class RaceSelectScreen extends Screen {
    private final List<Race> races = Arrays.asList(Race.values());
    private int index = 0;
    private Button left;
    private Button right;
    private Button done;

    public RaceSelectScreen() {
        super(Component.translatable("screen.acairsoriginssecundus.select_race"));
    }

    @Override
    protected void init() {
        int centerX = this.width / 2;
        int centerY = this.height / 2;
        // Кнопки для переключения рас
        this.left = this.addRenderableWidget(Button.builder(Component.literal("<"), b -> this.cycle(-1))
                .bounds(centerX - 70, centerY - 20, 20, 20).build());
        this.right = this.addRenderableWidget(Button.builder(Component.literal(">"), b -> this.cycle(1))
                .bounds(centerX + 50, centerY - 20, 20, 20).build());
        // Кнопка подтверждения выбора
        this.done = this.addRenderableWidget(Button.builder(Component.translatable("gui.done"), b -> this.confirm())
                .bounds(centerX - 50, centerY + 40, 100, 20).build());
    }

    private void cycle(int dir) {
        this.index = (this.index + dir + this.races.size()) % this.races.size();
    }

    private void confirm() {
        this.minecraft.setScreen(new CharacterEditorScreen(this.races.get(this.index)));
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(graphics);
        int centerX = this.width / 2;
        int centerY = this.height / 2;
        Race current = this.races.get(this.index);
        // Отрисовка модели игрока слева
        InventoryScreen.renderEntityInInventoryFollowsMouse(graphics, centerX - 90, centerY + 30, 30,
                (float)(centerX - 90) - mouseX, (float)(centerY + 30 - 60) - mouseY, Minecraft.getInstance().player);
        // Отрисовка названия и описания расы справа
        graphics.drawCenteredString(this.font, current.getName(), centerX, centerY - 60, 0xFFFFFF);
        graphics.drawWordWrap(this.font, current.getDescription(), centerX + 40, centerY - 20, 120, 0xFFFFFF);
        super.render(graphics, mouseX, mouseY, partialTick);
    }
}
