package com.acair.acairsoriginssecundus.client;

import com.acair.acairsoriginssecundus.client.screen.RaceSelectScreen;
import com.acair.acairsoriginssecundus.acairsoriginssecundus.Acairsoriginssecundus;
import net.minecraft.client.Minecraft;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.gui.screens.Screen;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;

/**
 * Класс для регистрации и обработки горячих клавиш клиента.
 * Используем один биндинг для открытия экрана выбора расы.
 * <p>Аннотация ниже указывает, что обработчики этого класса
 * слушают события на основной шине Forge только на клиенте.
 * Событие регистрации клавиш {@link RegisterKeyMappingsEvent}
 * отправляется на мод-шину, поэтому соответствующий метод
 * подключается к ней в конструкторе мода.</p>
 */
// Мы регистрируем обработчики событий вручную в конструкторе мода,
// поэтому аннотация EventBusSubscriber здесь не требуется.
 */
@Mod.EventBusSubscriber(modid = Acairsoriginssecundus.MODID, value = Dist.CLIENT)
public class KeyBindings {
    // Горячая клавиша лениво инициализируется при регистрации
    public static final Lazy<KeyMapping> OPEN_EDITOR = Lazy.of(() -> new KeyMapping(
            "key.acairsoriginssecundus.open_editor",
            GLFW.GLFW_KEY_O,
            "key.categories.acairsoriginssecundus"
    ));

    @SubscribeEvent
    public static void register(RegisterKeyMappingsEvent event) {
        event.register(OPEN_EDITOR.get());
    }

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            while (OPEN_EDITOR.get().consumeClick()) {
                Minecraft mc = Minecraft.getInstance();
                Screen current = mc.screen;
                if (!(current instanceof RaceSelectScreen)) {
                    mc.setScreen(new RaceSelectScreen());
                }
            }
        }
    }
}
