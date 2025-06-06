package com.acair.acairsoriginssecundus.client;

import com.acair.acairsoriginssecundus.client.screen.RaceSelectScreen;
import com.acair.acairsoriginssecundus.acairsoriginssecundus.Acairsoriginssecundus;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Отдельный обработчик нажатий клавиш.
 * Подписывается на события тиков клиента через Forge шину.
 */
@Mod.EventBusSubscriber(modid = Acairsoriginssecundus.MODID, value = Dist.CLIENT)
public class KeyInputHandler {
    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            while (KeyBindings.OPEN_EDITOR.get().consumeClick()) {
                Minecraft mc = Minecraft.getInstance();
                Screen current = mc.screen;
                if (!(current instanceof RaceSelectScreen)) {
                    mc.setScreen(new RaceSelectScreen());
                }
            }
        }
    }
}
