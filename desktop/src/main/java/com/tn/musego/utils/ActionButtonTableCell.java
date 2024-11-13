package com.tn.musego.utils;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

import java.util.function.UnaryOperator;

public class ActionButtonTableCell<S> extends TableCell<S, Button> {

    private final Button actionButton;

    public ActionButtonTableCell(String label, UnaryOperator<S> function) {
        this.getStyleClass().add("action-button-table-cell");

        this.actionButton = new Button(label);
        this.actionButton.setOnAction((ActionEvent e) -> function.apply(getCurrentItem()));
        this.actionButton.setMaxWidth(Double.MAX_VALUE);
    }//bouton modifier/supprimer

    public static <S> Callback<TableColumn<S, Button>, TableCell<S, Button>> forTableColumn(String label, UnaryOperator<S> function) {
        return param -> new ActionButtonTableCell<>(label, function);
    }

    public S getCurrentItem() {//récupérer l'item dans la liste
        return (S) getTableView().getItems().get(getIndex());
    }

    @Override
    public void updateItem(Button item, boolean empty) {//méthode pour update le cell
        super.updateItem(item, empty);

        if (empty) {
            setGraphic(null);
        } else {
            setGraphic(actionButton);
        }
    }
}