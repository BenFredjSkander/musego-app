package com.tn.musego.entities.enums;

import java.util.Arrays;

public enum Niveau {
    AVANCE("avance"),
    INTERMEDIAIRE("intermediaire"),
    DEBUTANT("debutant");

    public final String label;

    private Niveau(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    @Override
    public String toString() {
        return label;
    }

    public static Niveau getByValue(String value) {
        return Arrays.stream(Niveau.values()).filter(wallet -> wallet.getLabel().equals(value)).findFirst().get();
    }
}
