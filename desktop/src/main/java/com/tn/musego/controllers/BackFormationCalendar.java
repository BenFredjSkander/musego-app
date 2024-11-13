package com.tn.musego.controllers;

import com.calendarfx.model.Calendar;
import com.calendarfx.model.CalendarSource;
import com.calendarfx.model.Entry;
import com.calendarfx.view.page.MonthPage;
import com.tn.musego.entities.Formation;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

/**
 * @author Skander Ben Fredj
 * @created 05-Mar-23
 * @project musego
 */

public class BackFormationCalendar implements Initializable {

    ObservableList<Formation> listFormations;

    List<Entry<String>> formationEntries = new ArrayList<>();
    Calendar calendar = new Calendar("Test");
    CalendarSource calendarSource = new CalendarSource("source");
    @FXML
    private MonthPage monthPage;
    @FXML
    private AnchorPane root;

    @Override
    public void initialize(URL location, ResourceBundle resources) {


    }

    public void initCalendarEntries(ObservableList<Formation> formations) {
        this.listFormations = formations;
        formationEntries = formations.stream().map(this::mapToEntry).collect(Collectors.toList());
        calendar.addEntries(formationEntries);
        calendar.setReadOnly(true);
        calendar.setStyle(Calendar.Style.STYLE2);
        calendarSource.getCalendars().addAll(calendar);
        monthPage.getCalendarSources().setAll(calendarSource);

    }

    private Entry<String> mapToEntry(Formation formation) {
        Entry<String> entry = new Entry<>(formation.getNom());
        entry.setFullDay(true);
        entry.changeStartDate(Instant.ofEpochMilli(formation.getDateDebut().getTime()).atZone(ZoneId.systemDefault()).toLocalDate());
        entry.changeEndDate(Instant.ofEpochMilli(formation.getDateFin().getTime()).atZone(ZoneId.systemDefault()).toLocalDate());
        return entry;
    }
}
