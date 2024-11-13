module com.tn.musego {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires kernel;
    requires layout;
    requires io;
    requires javafx.graphics;
    requires javafx.base;
    requires javafx.swing;
    requires lombok;
    requires bcrypt;
    requires java.prefs;
    requires java.desktop;
    requires org.kordamp.ikonli.javafx;
    requires commons.validator;
    requires org.kordamp.ikonli.core;
    requires org.kordamp.ikonli.feather;
    requires com.jfoenix;
    requires sib.api.v3.sdk;
    requires twilio;
    requires org.apache.commons.io;
    requires AnimateFX;
    requires javafx.media;
    requires org.kordamp.ikonli.carbonicons;
    requires com.calendarfx.view;
    requires stripe.java;
    requires org.apache.commons.lang3;
    requires cloudinary.core;
    requires org.json;


    opens com.tn.musego to javafx.fxml;
    exports com.tn.musego;

    opens com.tn.musego.controllers to javafx.fxml;
    opens com.tn.musego.entities to javafx.base;


}
