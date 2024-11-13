package com.tn.musego.controllers;

import com.tn.musego.utils.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class GraphiqueEvents extends Stage {
    Connection connection;

    public GraphiqueEvents() {
        super();
        connection = DBConnection.getInstance().getConnection();
    }

    public void afficherGraphique() {
        try {


            String query = "SELECT nom, nb_participants FROM evenement"; // requête SQL pour récupérer les données

            PreparedStatement pstmt = connection.prepareStatement(query);
            ResultSet rs = pstmt.executeQuery(); // exécution de la requête

            CategoryAxis xAxis = new CategoryAxis(); // créer un axe pour les noms des événements
            NumberAxis yAxis = new NumberAxis(); // créer un axe pour le nombre de participants

            BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis); // créer un graphique de type BarChart
            barChart.setTitle("Nombre de participants par événement");

            XYChart.Series<String, Number> dataSeries = new XYChart.Series<>(); // créer une série de données pour le graphique

            while (rs.next()) {
                String eventName = rs.getString("nom");
                int nbParticipants = rs.getInt("nb_participants");

                dataSeries.getData().add(new XYChart.Data<>(eventName, nbParticipants)); // ajouter les données à la série
            }

            ObservableList<XYChart.Series<String, Number>> barChartData = FXCollections.observableArrayList();
            barChartData.add(dataSeries); // ajouter la série de données au graphique

            barChart.setData(barChartData); // définir les données du graphique

            StackPane root = new StackPane();
            root.getChildren().add(barChart);

            Scene scene = new Scene(root, 800, 600);
            this.setScene(scene);
            this.show();


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void afficherGraphiqueParDate() {
        try {


            String query = "SELECT date_debut, SUM(nb_participants) AS totalParticipants FROM evenement GROUP BY date_debut"; // requête SQL pour récupérer les données

            PreparedStatement pstmt = connection.prepareStatement(query);
            ResultSet rs = pstmt.executeQuery(); // exécution de la requête

            CategoryAxis xAxis = new CategoryAxis(); // créer un axe pour les dates des événements
            NumberAxis yAxis = new NumberAxis(); // créer un axe pour le nombre de participants

            LineChart<String, Number> lineChart = new LineChart<>(xAxis, yAxis); // créer un graphique de type LineChart
            lineChart.setTitle("Nombre de participants par date");

            XYChart.Series<String, Number> dataSeries = new XYChart.Series<>(); // créer une série de données pour le graphique

            while (rs.next()) {
                Date eventDate = rs.getDate("date_debut");
                int totalParticipants = rs.getInt("totalParticipants");

                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                String dateString = formatter.format(eventDate); // convertir la date en chaîne de caractères au format "yyyy-MM-dd"

                dataSeries.getData().add(new XYChart.Data<>(dateString, totalParticipants)); // ajouter les données à la série
            }

            ObservableList<XYChart.Series<String, Number>> lineChartData = FXCollections.observableArrayList();
            lineChartData.add(dataSeries); // ajouter la série de données au graphique

            lineChart.setData(lineChartData); // définir les données du graphique

            StackPane root = new StackPane();
            root.getChildren().add(lineChart);

            Scene scene = new Scene(root, 800, 600);
            this.setScene(scene);
            this.show();


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
