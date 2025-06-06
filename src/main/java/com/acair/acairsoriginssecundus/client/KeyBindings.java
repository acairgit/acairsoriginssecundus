package com.acair.acairsoriginssecundus.client;

import com.acair.acairsoriginssecundus.acairsoriginssecundus.Acairsoriginssecundus;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

/**
 * Регистрация клавиш клиента.
 * <p>
 * Биндинг хранится в {@link Lazy}, поэтому объект создаётся
 * только во время регистрации.
 */
@Mod.EventBusSubscriber(modid = Acairsoriginssecundus.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class KeyBindings {
    /**
     * Клавиша открытия редактора персонажа.
     */
    public static final Lazy<KeyMapping> OPEN_EDITOR = Lazy.of(() -> new KeyMapping(
            "key.acairsoriginssecundus.open_editor",
            GLFW.GLFW_KEY_O,
            "key.categories.acairsoriginssecundus"
    ));

    @SubscribeEvent
    public static void register(RegisterKeyMappingsEvent event) {
        event.register(OPEN_EDITOR.get());
    }
}
