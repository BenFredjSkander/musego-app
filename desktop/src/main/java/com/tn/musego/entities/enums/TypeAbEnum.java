package com.tn.musego.entities.enums;

import java.util.Arrays;

public enum TypeAbEnum {
    HEBDOMADAIRE("Hebdomadaire"),
    MENSUEL("Mensuel"),
    ANNUEL("Annuel");

    public final String label;

    private TypeAbEnum(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    @Override
    public String toString() {
        return label;
    }

    public static TypeAbEnum getByValue(String value) {
        return Arrays.stream(TypeAbEnum.values()).filter(wallet -> wallet.getLabel().equals(value)).findFirst().get();
    }
}
