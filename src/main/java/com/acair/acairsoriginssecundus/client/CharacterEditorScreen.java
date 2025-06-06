package com.acair.acairsoriginssecundus.client;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

/**
 * Экран простого редактора персонажа.
 * Здесь пока только фон и кнопка закрытия.
 * Реальный функционал будет добавлен позже.
 */
public class CharacterEditorScreen extends Screen {

    // Фон берем из текстур Origins.
    private static final ResourceLocation BACKGROUND =
            new ResourceLocation("acairsoriginssecundus", "origins/textures/gui/choose_origin.png");

    private Button doneButton;

    public CharacterEditorScreen() {
        super(Component.translatable("screen.acairsoriginssecundus.editor"));
    }

    @Override
    protected void init() {
        int centerX = this.width / 2;
        int centerY = this.height / 2;
        // Кнопка закрытия использует стандартный перевод gui.done
        doneButton = Button.builder(Component.translatable("gui.done"), button -> onClose())
                .bounds(centerX - 50, centerY + 70, 100, 20)
                .build();
        addRenderableWidget(doneButton);
    }

    @Override
    public void onClose() {
        this.minecraft.setScreen(null);
    }

    @Override
    public void render(@NotNull GuiGraphics gfx, int mouseX, int mouseY, float partialTicks) {
        // Отрисовка фона
        RenderSystem.setShaderTexture(0, BACKGROUND);
        gfx.blit(BACKGROUND, (this.width - 256) / 2, (this.height - 256) / 2, 0, 0, 256, 256);
        super.render(gfx, mouseX, mouseY, partialTicks);
    }
}
