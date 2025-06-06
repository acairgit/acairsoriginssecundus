package com.acair.acairsoriginssecundus.client;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.NotNull;

/**
 * Экран выбора расы. Реализует упрощенную версию меню Origins
 * и демонстрирует идею расположения элементов интерфейса.
 */
public class SelectRaceScreen extends Screen {

    // Используем текстуру из Origins для фона
    private static final ResourceLocation BACKGROUND =
            new ResourceLocation("acairsoriginssecundus", "origins/textures/gui/choose_origin.png");

    // Простое перечисление рас. Позже список будет получен из Origins API.
    private enum Race {
        HUMAN("race.acairsoriginssecundus.human", "race.acairsoriginssecundus.human.desc", new ItemStack(Items.BOOK)),
        ELF("race.acairsoriginssecundus.elf", "race.acairsoriginssecundus.elf.desc", new ItemStack(Items.OAK_SAPLING));

        final String nameKey;
        final String descKey;
        final ItemStack icon;

        Race(String nameKey, String descKey, ItemStack icon) {
            this.nameKey = nameKey;
            this.descKey = descKey;
            this.icon = icon;
        }
    }

    private int selectedIndex = 0;

    private Button doneButton;
    private Button backButton;

    public SelectRaceScreen() {
        super(Component.translatable("screen.acairsoriginssecundus.select_race"));
    }

    @Override
    protected void init() {
        int listLeft = 20;
        int listTop = 40;
        int spacing = 24;

        // Создаем кнопки-иконки для выбора расы
        for (int i = 0; i < Race.values().length; i++) {
            Race race = Race.values()[i];
            int idx = i;
            addRenderableWidget(Button.builder(Component.literal(""), b -> selectedIndex = idx)
                    .bounds(listLeft, listTop + i * spacing, 20, 20)
                    .build());
        }

        doneButton = addRenderableWidget(Button.builder(Component.translatable("gui.done"), b -> openEditor())
                .bounds(this.width - 110, this.height - 30, 100, 20)
                .build());
        backButton = addRenderableWidget(Button.builder(Component.translatable("gui.back"), b -> onClose())
                .bounds(this.width - 220, this.height - 30, 100, 20)
                .build());
        backButton.active = false; // пока возвращаться некуда
    }

    private void openEditor() {
        Minecraft.getInstance().setScreen(new CharacterEditorScreen());
    }

    @Override
    public void onClose() {
        this.minecraft.setScreen(null);
    }

    @Override
    public void render(@NotNull GuiGraphics gfx, int mouseX, int mouseY, float partialTicks) {
        RenderSystem.setShaderTexture(0, BACKGROUND);
        gfx.blit(BACKGROUND, (this.width - 256) / 2, (this.height - 256) / 2, 0, 0, 256, 256);

        super.render(gfx, mouseX, mouseY, partialTicks);

        // Отрисовываем иконки рас вручную, чтобы показать выбор
        int listLeft = 20;
        int listTop = 40;
        int spacing = 24;
        for (int i = 0; i < Race.values().length; i++) {
            Race race = Race.values()[i];
            int x = listLeft + 2;
            int y = listTop + i * spacing + 2;
            gfx.renderFakeItem(race.icon, x, y);
            if (i == selectedIndex) {
                gfx.drawString(font, ">", listLeft - 10, listTop + i * spacing + 6, 0xFFFFFF);
            }
        }

        // Отрисовываем модель игрока в центре
        int centerX = this.width / 2;
        int centerY = this.height / 2;
        InventoryScreen.renderEntityInInventoryFollowsMouse(gfx, centerX, centerY + 50, 40,
                centerX - mouseX, centerY - 50 - mouseY, Minecraft.getInstance().player);

        // Справа выводим описание выбранной расы
        Race race = Race.values()[selectedIndex];
        int descLeft = this.width - 160;
        gfx.drawString(font, Component.translatable(race.nameKey), descLeft, 40, 0xFFFFFF);
        font.split(Component.translatable(race.descKey), 150).forEach(line -> {
            gfx.drawString(font, line, descLeft, 60 + font.lineHeight * font.split(Component.literal(""), 1).size(), 0xCCCCCC);
        });
    }
}
