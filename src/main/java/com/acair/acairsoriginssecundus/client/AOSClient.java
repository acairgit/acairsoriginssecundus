package com.acair.acairsoriginssecundus.client;

import com.acair.acairsoriginssecundus.AcairsOriginsSecundus;
import net.minecraft.client.Minecraft;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.gui.screens.Screen;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.common.util.Lazy;
import com.mojang.blaze3d.platform.InputConstants;
import org.lwjgl.glfw.GLFW;

/**
 * Класс с клиентскими обработчиками событий: регистрация
 * клавиш и открытие экрана редактора персонажа.
 */
@Mod.EventBusSubscriber(modid = AcairsOriginsSecundus.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class AOSClient {

    // Лениво создаем KeyMapping. Клавиша по умолчанию - O.
    public static final Lazy<KeyMapping> OPEN_EDITOR_KEY = Lazy.of(() ->
            new KeyMapping("key.acairsoriginssecundus.open_editor", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_O,
                    "key.categories.acairsoriginssecundus"));

    // Регистрируем клавишу через событие RegisterKeyMappingsEvent
    @SubscribeEvent
    public static void registerBindings(RegisterKeyMappingsEvent event) {
        event.register(OPEN_EDITOR_KEY.get());
    }

    // Отслеживаем нажатие клавиши каждый тик клиента
    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            while (OPEN_EDITOR_KEY.get().consumeClick()) {
                Minecraft mc = Minecraft.getInstance();
                Screen current = mc.screen;
                if (current == null) {
                    mc.setScreen(new CharacterEditorScreen());
                }
            }
        }
    }
}
