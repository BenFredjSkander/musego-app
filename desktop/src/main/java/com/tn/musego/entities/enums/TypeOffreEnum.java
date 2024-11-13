package com.tn.musego.entities.enums;

import java.util.Arrays;

public enum TypeOffreEnum {
    BRONZE("Bronze"),
    SILVER("Silver"),
    GOLD("Gold");

    public final String label;

    private TypeOffreEnum(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    @Override
    public String toString() {
        return label;
    }

    public static TypeOffreEnum getByValue(String value) {
        return Arrays.stream(TypeOffreEnum.values()).filter(wallet -> wallet.getLabel().equals(value)).findFirst().get();
    }
}
