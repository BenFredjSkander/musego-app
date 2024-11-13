package com.tn.musego.services;

import com.tn.musego.entities.Attendance;

public interface IAttendanceService extends IBaseCrud<Attendance> {


    void supprimerAttendance(Long idUser, Long idFormation);

    boolean userRegistered(long userId, long formationId);
}