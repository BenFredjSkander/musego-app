package com.tn.musego.utils;

import com.tn.musego.utils.validator.*;
import javafx.geometry.Side;
import javafx.scene.control.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.EmailValidator;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Skander Ben Fredj
 * @created 26-Feb-23
 * @project musego
 */

public class ValidatorHelper {
    public static String errorBorderColor = "#FF0000";
    List<BaseValidator> fieldList;

    public ValidatorHelper() {
        fieldList = new ArrayList<>();
    }

    public void addValidator(BaseValidator validator) {
        fieldList.add(validator);
//        fieldList
        final ContextMenu textEmptyValidator = new ContextMenu();
        textEmptyValidator.setAutoHide(true);
//        return textEmptyValidator;
    }

    public boolean validateFields() {
        List<Boolean> valid = new ArrayList<>();
        for (BaseValidator validator : fieldList) {
            validator.field.setStyle(null);
        }
        for (BaseValidator validator : fieldList) {
            if (validator instanceof TextFieldValidator) {
                final ContextMenu contextMenuValidator = new ContextMenu();
                MenuItem menuItem = new MenuItem();
                menuItem.setText(validator.textError);
                if ((validator.field instanceof TextField) || (validator.field instanceof TextArea)) {
                    if (((TextField) validator.field).getText().trim().isEmpty()) {
                        contextMenuValidator.getItems().clear();
                        contextMenuValidator.getItems().add(menuItem);
                        contextMenuValidator.show(validator.field, Side.RIGHT, 10, 0);
                        validator.field.setStyle("-fx-border-color: " + errorBorderColor);
                    } else {
                        contextMenuValidator.hide();
                        validator.field.setStyle(null);
                        valid.add(true);
                    }
                }
            }

            if (validator instanceof TextAreaValidator) {
                final ContextMenu contextMenuValidator = new ContextMenu();
                MenuItem menuItem = new MenuItem();
                menuItem.setText(validator.textError);
                if (validator.field instanceof TextArea) {
                    if (((TextArea) validator.field).getText().trim().isEmpty()) {
                        contextMenuValidator.getItems().clear();
                        contextMenuValidator.getItems().add(menuItem);
                        contextMenuValidator.show(validator.field, Side.RIGHT, 10, 0);
                        validator.field.setStyle("-fx-border-color: " + errorBorderColor);
                    } else {
                        contextMenuValidator.hide();
                        validator.field.setStyle(null);
                        valid.add(true);
                    }
                }
            }

            if (validator instanceof EmailFieldValidator) {
                final ContextMenu contextMenuValidator = new ContextMenu();
                MenuItem menuItem = new MenuItem();
                if (((TextField) validator.field).getText().trim().isEmpty()) {
                    contextMenuValidator.getItems().clear();
                    menuItem.setText(StringCnst.champObligatoire);
                    contextMenuValidator.getItems().add(menuItem);
                    contextMenuValidator.show(validator.field, Side.RIGHT, 10, 0);
                    validator.field.setStyle("-fx-border-color: " + errorBorderColor);
                } else if (!EmailValidator.getInstance().isValid(((TextField) validator.field).getText())) {
                    contextMenuValidator.getItems().clear();
                    menuItem.setText(validator.textError);
                    contextMenuValidator.getItems().add(menuItem);
                    contextMenuValidator.show(validator.field, Side.RIGHT, 10, 0);
                    validator.field.setStyle("-fx-border-color: " + errorBorderColor);
                } else {
                    contextMenuValidator.hide();
                    validator.field.setStyle(null);
                    valid.add(true);
                }
            }

            if (validator instanceof PasswordFieldMatchValidator) {
                final ContextMenu contextMenuValidator1 = new ContextMenu();
                final ContextMenu contextMenuValidator2 = new ContextMenu();

                MenuItem menuItem1 = new MenuItem();
                MenuItem menuItem2 = new MenuItem();
                if (((PasswordField) validator.field).getText().trim().isEmpty() || ((PasswordField) ((PasswordFieldMatchValidator) validator).field2).getText().trim().isEmpty()) {
                    contextMenuValidator1.getItems().clear();
                    contextMenuValidator2.getItems().clear();
                    menuItem1.setText(StringCnst.champObligatoire);
                    menuItem2.setText(StringCnst.champObligatoire);
                    contextMenuValidator1.getItems().add(menuItem1);
                    contextMenuValidator2.getItems().add(menuItem2);
                    validator.field.setStyle("-fx-border-color: " + errorBorderColor);
                    ((PasswordFieldMatchValidator) validator).field2.setStyle("-fx-border-color: " + errorBorderColor);
                    contextMenuValidator1.show(validator.field, Side.RIGHT, 10, 0);
                    contextMenuValidator2.show(((PasswordFieldMatchValidator) validator).field2, Side.RIGHT, 10, 0);
                } else if (!((PasswordField) validator.field).getText().equals(((PasswordField) ((PasswordFieldMatchValidator) validator).field2).getText())) {
                    contextMenuValidator1.getItems().clear();
                    contextMenuValidator2.getItems().clear();
                    menuItem2.setText(StringCnst.passDontMatch);
                    contextMenuValidator2.getItems().add(menuItem2);
                    ((PasswordFieldMatchValidator) validator).field2.setStyle("-fx-border-color: " + errorBorderColor);
                    contextMenuValidator2.show(((PasswordFieldMatchValidator) validator).field2, Side.RIGHT, 10, 0);

                } else {
                    contextMenuValidator1.hide();
                    contextMenuValidator2.hide();
                    validator.field.setStyle(null);
                    ((PasswordFieldMatchValidator) validator).field2.setStyle(null);
                    valid.add(true);

                }
            }

            if (validator instanceof DateAfterValidator) {
                final ContextMenu contextMenuValidator1 = new ContextMenu();
                final ContextMenu contextMenuValidator2 = new ContextMenu();

                MenuItem menuItem1 = new MenuItem();
                MenuItem menuItem2 = new MenuItem();
                if (((DatePicker) validator.field).getValue() == null || ((DatePicker) ((DateAfterValidator) validator).field2).getValue() == null) {
                    contextMenuValidator1.getItems().clear();
                    contextMenuValidator2.getItems().clear();
                    menuItem1.setText(StringCnst.champObligatoire);
                    menuItem2.setText(StringCnst.champObligatoire);
                    contextMenuValidator1.getItems().add(menuItem1);
                    contextMenuValidator2.getItems().add(menuItem2);
                    validator.field.setStyle("-fx-border-color: " + errorBorderColor);
                    ((DateAfterValidator) validator).field2.setStyle("-fx-border-color: " + errorBorderColor);
                    contextMenuValidator1.show(validator.field, Side.RIGHT, 10, 0);
                    contextMenuValidator2.show(((DateAfterValidator) validator).field2, Side.RIGHT, 10, 0);
                } else if (((DatePicker) validator.field).getValue().isAfter(((DatePicker) ((DateAfterValidator) validator).field2).getValue())) {
                    contextMenuValidator1.getItems().clear();
                    contextMenuValidator2.getItems().clear();
                    menuItem2.setText(validator.textError);
                    contextMenuValidator2.getItems().add(menuItem2);
                    ((DateAfterValidator) validator).field2.setStyle("-fx-border-color: " + errorBorderColor);
                    contextMenuValidator2.show(((DateAfterValidator) validator).field2, Side.RIGHT, 10, 0);

                } else {
                    contextMenuValidator1.hide();
                    contextMenuValidator2.hide();
                    validator.field.setStyle(null);
                    ((DateAfterValidator) validator).field2.setStyle(null);
                    valid.add(true);

                }
            }

            if (validator instanceof ChoiceValidator) {
                final ContextMenu contextMenuValidator = new ContextMenu();
                MenuItem menuItem = new MenuItem();
                menuItem.setText(validator.textError);
                if ((validator.field instanceof ChoiceBox<?>) || (validator.field instanceof ComboBox<?>)) {
                    if (((ComboBox) validator.field).getValue() == null) {
                        contextMenuValidator.getItems().clear();
                        contextMenuValidator.getItems().add(menuItem);
                        contextMenuValidator.show(validator.field, Side.RIGHT, 10, 0);
                        validator.field.setStyle("-fx-border-color: " + errorBorderColor);
                    } else {
                        contextMenuValidator.hide();
                        validator.field.setStyle(null);
                        valid.add(true);
                    }
                }
            }

            if (validator instanceof DatepickerValidator) {
                final ContextMenu contextMenuValidator = new ContextMenu();
                MenuItem menuItem = new MenuItem();
                menuItem.setText(validator.textError);
                if ((validator.field instanceof DatePicker)) {
                    if (((DatePicker) validator.field).getValue() == null) {
                        contextMenuValidator.getItems().clear();
                        contextMenuValidator.getItems().add(menuItem);
                        contextMenuValidator.show(validator.field, Side.RIGHT, 10, 0);
                        validator.field.setStyle("-fx-border-color: " + errorBorderColor);
                    } else {
                        contextMenuValidator.hide();
                        validator.field.setStyle(null);
                        valid.add(true);
                    }
                }
            }

            if (validator instanceof NumberFieldValidator) {
                final ContextMenu contextMenuValidator = new ContextMenu();
                MenuItem menuItem = new MenuItem();
                menuItem.setText(validator.textError);
                if ((validator.field instanceof TextField)) {
                    if (((TextField) validator.field).getText().trim().isEmpty()) {
                        menuItem.setText(StringCnst.champObligatoire);
                        contextMenuValidator.getItems().clear();
                        contextMenuValidator.getItems().add(menuItem);
                        contextMenuValidator.show(validator.field, Side.RIGHT, 10, 0);
                        validator.field.setStyle("-fx-border-color: " + errorBorderColor);
                    } else if (!StringUtils.isNumeric(((TextField) validator.field).getText())) {
                        contextMenuValidator.getItems().clear();
                        contextMenuValidator.getItems().add(menuItem);
                        contextMenuValidator.show(validator.field, Side.RIGHT, 10, 0);
                        validator.field.setStyle("-fx-border-color: " + errorBorderColor);
                    } else if (((TextField) validator.field).getText().length() != validator.maxLength) {
                        menuItem.setText("La longueur doit être égale à " + validator.maxLength);
                        contextMenuValidator.getItems().clear();
                        contextMenuValidator.getItems().add(menuItem);
                        contextMenuValidator.show(validator.field, Side.RIGHT, 10, 0);
                        validator.field.setStyle("-fx-border-color: " + errorBorderColor);
                    } else {
                        contextMenuValidator.hide();
                        validator.field.setStyle(null);
                        valid.add(true);
                    }
                }
            }
        }
        return valid.size() == fieldList.size();
    }
}

