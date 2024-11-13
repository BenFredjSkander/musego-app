//package com.tn.musego.services.impl;
//
//import com.tn.musego.entities.Integration;
//import com.tn.musego.exceptions.MyCustomException;
//import com.tn.musego.services.IIntegrationService;
//import com.tn.musego.utils.DBConnection;
//import javafx.collections.FXCollections;
//import javafx.collections.ObservableList;
//
//import java.sql.*;
//
//public class IntegrationService implements IIntegrationService {
//    Connection connection;
//
//    public IntegrationService() {
//        this.connection = new DBConnection().getConnection();
//    }
//
//    @Override
//    public void createEntity(Integration i) {
//        String requete = "INSERT INTO `Integration` (id_user,id_formation) VALUES (?,?)";
//        try {
//            PreparedStatement pst = connection.prepareStatement(requete);
//            pst.setInt(1, i.getIdUser());
//            pst.setInt(2, i.getIdFormation());
//            pst.executeUpdate();
//            System.out.println("Integration Ajoutée");
//        } catch (SQLException ex) {
//            System.err.println(ex.getMessage());
//        }
//
//    }
//
//    @Override
//    public Integration getEntityByID(Long id) throws MyCustomException {
//        return null;
//    }
//
//    @Override
//    public ObservableList<Integration> getAll() {
//        ObservableList<Integration> myList = FXCollections.observableArrayList();
//
//        try {
//            String requete = "SELECT * FROM `integration`";
//            Statement pst = connection.createStatement();
//            ResultSet rs = pst.executeQuery(requete);
//            while (rs.next()) {
//                Integration i = new Integration().mapFromResulSet(rs);
//                myList.add(i);
//
//            }
//        } catch (SQLException ex) {
//            System.err.println(ex.getMessage());
//        }
//
//        return myList;
//
//    }
//
//    @Override
//    public void updateEntity(Integration integration) {
//
//    }
//
//    @Override
//    public void deleteEntityById(Long id) {
//
//    }
//
//    @Override
//    public void supprimerIntegration(int id_u, int id_formation) {
//        String requete = "DELETE FROM integration WHERE id_user =? and id_formation =?";
//
//        try {
//            PreparedStatement pst = connection.prepareStatement(requete);
//            pst.setInt(1, id_u);
//            pst.setInt(2, id_formation);
//            pst.executeUpdate();
//            System.out.println("Suppression Integration confirmée!");
//        } catch (SQLException ex) {
//            System.err.println(ex.getMessage());
//        }
//
//    }
//
//}
