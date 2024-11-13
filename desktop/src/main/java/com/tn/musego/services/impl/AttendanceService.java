package com.tn.musego.services.impl;

import com.tn.musego.entities.Attendance;
import com.tn.musego.exceptions.MyCustomException;
import com.tn.musego.services.IAttendanceService;
import com.tn.musego.utils.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.extern.java.Log;

import java.sql.*;

@Log
public class AttendanceService implements IAttendanceService {
    Connection connection;

    public AttendanceService() {
        this.connection = new DBConnection().getConnection();
    }


    @Override
    public void createEntity(Attendance a) {
        String requete = "INSERT INTO `attendance` (id_user,id_formation) VALUES (?,?)";
        try {
            PreparedStatement pst = connection.prepareStatement(requete);
            pst.setLong(1, a.getIdUser());
            pst.setLong(2, a.getIdFormation());
            log.info(pst.toString());
            pst.executeUpdate();

            System.out.println("attendance ajouté avec succes ! ");

        } catch (SQLException ex) {
            log.severe(ex.getMessage());
        }

    }

    @Override
    public Attendance getEntityByID(Long id) throws MyCustomException {
        return null;
    }

    @Override
    public ObservableList<Attendance> getAll() {
        ObservableList<Attendance> myList = FXCollections.observableArrayList();

        try {
            String requete = "SELECT * FROM `attendance`";
            Statement pst = connection.createStatement();
            ResultSet rs = pst.executeQuery(requete);
            while (rs.next()) {
                Attendance a = new Attendance().mapFromResultset(rs);
                myList.add(a);

            }
        } catch (SQLException ex) {
            log.severe(ex.getMessage());
        }

        return myList;

    }

    @Override
    public void updateEntity(Attendance attendance) {

    }

    @Override
    public void deleteEntityById(Long id) {

    }

//    @Override
//    public void ajouterAttendance(Long id_user, long id_formation) {
//
//        try {
//
//            String requete = "INSERT INTO attendance (id_user,id_formation) " + "VALUES (?,?);";
//            PreparedStatement pst = connection.prepareStatement(requete);
//            pst.setLong(1, id_user);
//            pst.setLong(2, id_formation);
//            pst.executeUpdate();
//
//
//            String requete2 = "UPDATE `formation` SET nb_inscrits = nb_inscrits + 1 where id =" + id_formation;
//            Statement pst2 = connection.createStatement();
//            pst2.executeUpdate(requete2);
//            System.out.println("attendance ajouté avec succes ! ");
//        } catch (SQLException ex) {
//            System.err.println(ex.getMessage());
//        }
//    }

    @Override
    public void supprimerAttendance(Long idUser, Long idFormation) {
        String requete = "DELETE FROM attendance WHERE id_user =? and id_formation =?";

        try {
            PreparedStatement pst = connection.prepareStatement(requete);
            pst.setLong(1, idUser);
            pst.setLong(2, idFormation);
            pst.executeUpdate();
            System.out.println("Suppression Attendance confirmée!");
        } catch (SQLException ex) {
            log.severe(ex.getMessage());
        }

    }

    @Override
    public boolean userRegistered(long userId, long formationId) {
        try {
            String query = "SELECT * FROM attendance WHERE id_user = ? AND id_formation = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setLong(1, userId);
            preparedStatement.setLong(2, formationId);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return true;
            }

        } catch (SQLException ex) {
            log.severe(ex.getMessage());
        }
        return false;
    }
}
