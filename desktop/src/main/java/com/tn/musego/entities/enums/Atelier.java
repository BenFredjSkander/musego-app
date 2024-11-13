//package com.tn.musego.entities.enums;
//
//import java.util.Arrays;
//
//public enum Atelier {
//    POTERIE("Poterie"),
//    THEATRE("Theatre"),
//    SCULPTURE("Sculpture"),
//    DESSIN("Dessin"),
//    PHOTOGRAPHIE("Photographie");
//
//    public final String label;
//
//    private Atelier(String label) {
//        this.label = label;
//    }
//
//    public String getLabel() {
//        return label;
//    }
//
//    @Override
//    public String toString() {
//        return label;
//    }
//
//    public static Atelier getByValue(String value) {
//        return Arrays.stream(Atelier.values()).filter(wallet -> wallet.getLabel().equals(value)).findFirst().get();
//    }
//}
