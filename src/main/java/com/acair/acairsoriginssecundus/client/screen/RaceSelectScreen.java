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
 * Простой экран для выбора расы персонажа.
 */
public class RaceSelectScreen extends Screen {
    /** Список доступных рас */
    private final List<Race> races = Arrays.asList(Race.values());
    private RaceList raceList;

    public RaceSelectScreen() {
        super(Component.translatable("screen.acairsoriginssecundus.select_race"));
    }

    @Override
    protected void init() {
        int listWidth = 80;
        raceList = new RaceList(this.minecraft, 20, 20, listWidth, this.height - 40, races);
        addRenderableWidget(raceList);

        addRenderableWidget(Button.builder(Component.translatable("gui.done"), b -> confirm())
                .bounds(this.width / 2 - 50, this.height - 30, 100, 20).build());
    }

    private void confirm() {
        this.minecraft.setScreen(new CharacterEditorScreen(raceList.getSelectedRace()));
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        renderDirtBackground(graphics);

        Race current = raceList.getSelectedRace();
        int modelX = this.width / 2 - 30;
        int modelY = this.height / 2 + 40;
        InventoryScreen.renderEntityInInventoryFollowsMouse(graphics, modelX, modelY, 50,
                modelX - mouseX, modelY - 50 - mouseY, Minecraft.getInstance().player);

        int infoLeft = this.width / 2 + 40;
        graphics.drawString(this.font, current.getName(), infoLeft, 40, 0xFFFFFF, false);
        graphics.drawWordWrap(this.font, current.getDescription(), infoLeft, 60, 120, 0xFFFFFF);

        super.render(graphics, mouseX, mouseY, partialTick);
    }
}
