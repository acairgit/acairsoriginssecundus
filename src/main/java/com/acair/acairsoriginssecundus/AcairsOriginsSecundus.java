package com.acair.acairsoriginssecundus;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.acair.acairsoriginssecundus.client.AOSClient;

/**
 * Основной класс мода. Здесь происходит регистрация событий и
 * инициализация клиентской части при необходимости.
 */
@Mod(AcairsOriginsSecundus.MODID)
public class AcairsOriginsSecundus {

    public static final String MODID = "acairsoriginssecundus";
    public static final Logger LOGGER = LogManager.getLogger();

    public AcairsOriginsSecundus() {
        LOGGER.info("Acair's Origins Secundus initializing");
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        // Клиентскую инициализацию выполняем только на клиенте
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> modBus.addListener(this::clientSetup));
    }

    private void clientSetup(final FMLClientSetupEvent event) {
        // В клиентскую инициализацию ничего не помещаем, она нужна только чтобы
        // гарантировать загрузку класса AOSClient и его подписок на события
        AOSClient.OPEN_EDITOR_KEY.get();
    }
}
