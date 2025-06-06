package com.acair.acairsoriginssecundus.character;

import net.minecraft.network.chat.Component;

/**
 * Перечисление рас для персонажей. Каждая раса содержит
 * название и описание для отображения в интерфейсе.
 */
public enum Race {
    HUMAN("race.acairsoriginssecundus.human", "race.acairsoriginssecundus.human.desc"),
    ELF("race.acairsoriginssecundus.elf", "race.acairsoriginssecundus.elf.desc");

    private final String nameKey;
    private final String descriptionKey;

    Race(String nameKey, String descriptionKey) {
        this.nameKey = nameKey;
        this.descriptionKey = descriptionKey;
    }

    public Component getName() {
        return Component.translatable(this.nameKey);
    }

    public Component getDescription() {
        return Component.translatable(this.descriptionKey);
    }
}
