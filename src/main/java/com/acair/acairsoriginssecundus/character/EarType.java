package com.acair.acairsoriginssecundus.character;

import net.minecraft.network.chat.Component;

/**
 * Тип ушей используется для рас,
 * которым доступна подобная настройка
 * (например, эльфам).
 */
public enum EarType {
    HUMAN("ear_type.acairsoriginssecundus.human"),
    POINTED("ear_type.acairsoriginssecundus.pointed");

    private final String key;

    EarType(String key) {
        this.key = key;
    }

    public Component getName() {
        return Component.translatable(this.key);
    }
}
