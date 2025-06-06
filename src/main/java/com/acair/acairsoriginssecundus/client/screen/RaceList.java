package com.acair.acairsoriginssecundus.client.screen;

import com.acair.acairsoriginssecundus.character.Race;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraftforge.client.gui.widget.ScrollPanel;
import com.mojang.blaze3d.vertex.Tesselator;
import net.minecraft.network.chat.Component;

import java.util.List;

/**
 * Простой список рас с возможностью прокрутки.
 * Использует ScrollPanel из Forge для реализации полосы прокрутки.
 */
public class RaceList extends ScrollPanel {
    private final List<Race> races;
    private int selected;

    public RaceList(Minecraft mc, int left, int top, int width, int height, List<Race> races) {
        super(mc, width, height, top, left);
        this.races = races;
    }

    /**
     * Устанавливает текущий выбранный индекс согласно переданной расе.
     */
    public void setSelectedRace(Race race) {
        this.selected = races.indexOf(race);
    }

    /**
     * Возвращает выбранную расу.
     */
    public Race getSelectedRace() {
        if (selected < 0 || selected >= races.size()) {
            return races.get(0);
        }
        return races.get(selected);
    }

    @Override
    protected int getContentHeight() {
        // Высота всего содержимого равна количеству строк умножить на высоту шрифта
        Font font = Minecraft.getInstance().font;
        return races.size() * (font.lineHeight + 4);
    }

    @Override
    protected void drawPanel(GuiGraphics graphics, int entryRight, int relativeY, Tesselator tess, int mouseX, int mouseY) {
        Font font = Minecraft.getInstance().font;
        int y = relativeY;
        for (int i = 0; i < races.size(); i++) {
            Component name = races.get(i).getName();
            int color = (i == selected) ? 0xFFFFA0 : 0xFFFFFF;
            graphics.drawString(font, name, this.left + 4, y, color, false);
            y += font.lineHeight + 4;
        }
    }

    @Override
    protected boolean clickPanel(double mouseX, double mouseY, int button) {
        Font font = Minecraft.getInstance().font;
        int index = (int)(mouseY / (font.lineHeight + 4));
        if (index >= 0 && index < races.size()) {
            this.selected = index;
            return true;
        }
        return false;
    }
}
