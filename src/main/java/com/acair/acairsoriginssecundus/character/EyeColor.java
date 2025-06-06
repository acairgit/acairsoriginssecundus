package com.acair.acairsoriginssecundus.character;

import net.minecraft.network.chat.Component;

/**
 * Цвет глаз персонажа. Используется в редакторе
 * для выбора из доступных вариантов.
 */
public enum EyeColor {
    BROWN("eye_color.acairsoriginssecundus.brown"),
    GREEN("eye_color.acairsoriginssecundus.green"),
    BLUE("eye_color.acairsoriginssecundus.blue");

    private final String key;

    EyeColor(String key) {
        this.key = key;
    }

    /**
     * @return локализованное название цвета
     */
    public Component getName() {
        return Component.translatable(this.key);
    }
}
