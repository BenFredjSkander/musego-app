package com.tn.musego.services.impl;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import com.stripe.model.Customer;
import com.stripe.param.ChargeCreateParams;
import com.stripe.param.CustomerCreateParams;
import com.tn.musego.entities.PaymentCard;
import com.tn.musego.exceptions.MyCustomException;
import com.tn.musego.services.IBaseCrud;
import com.tn.musego.services.IPaymentService;
import com.tn.musego.utils.DBConnection;
import com.tn.musego.utils.FunctionHelper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.extern.java.Log;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Log
public class PaymentService implements IPaymentService, IBaseCrud<PaymentCard> {
    Connection connection = null;

    public PaymentService() {
        Stripe.apiKey = new FunctionHelper().getProperty("project.properties", "striptestkey");
        this.connection = new DBConnection().getConnection();
    }

    /////////////////////
    @Override
    public String createNewCustomer(String source, String email, String username) throws StripeException {
        CustomerCreateParams customerParams =
                CustomerCreateParams.builder()
                        .setSource(source)
                        .setEmail(email)
                        .setName(username)
                        .build();

        Customer customer = Customer.create(customerParams);
        return customer.getId();
    }

    @Override
    public void chargeNewCustomer(String customerId, long amount) throws StripeException {
        ChargeCreateParams chargeParams =
                ChargeCreateParams.builder()
                        .setAmount(amount)
                        .setCurrency("eur")
                        .setCustomer(customerId)
                        .build();

        Charge charge = Charge.create(chargeParams);
    }

//    @Override
//    public void changePaymentMethod(String id,String method) throws StripeException {
//        Customer resource = Customer.retrieve(id);
//        CustomerUpdateParams params =
//                CustomerUpdateParams.builder().setSource(method).build();
//
//        Customer customer = resource.update(params);
//    }

    //////////////////

    @Override
    public void createEntity(PaymentCard paymentCard) {
        String req = "insert into payment_card (customer_id,type,balance, id_user) " +
                "values (?,?,?,?)";
        try {
            PreparedStatement statement = connection.prepareStatement(req);
            statement.setString(1, paymentCard.getCustomerId());
            statement.setString(2, paymentCard.getType().toString());
            statement.setDouble(3, paymentCard.getBalance());
            statement.setLong(4, paymentCard.getUserId());
            statement.executeUpdate();
            statement.close();
            log.info("payment card created successfully");
        } catch (SQLException e) {
            log.severe(e.getMessage());
        }
    }

    @Override
    public PaymentCard getEntityByID(Long id) {
        String req = "select * from payment_card where id = ?";
        PaymentCard paymentCard = new PaymentCard();

        try {
            PreparedStatement statement = connection.prepareStatement(req);
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                throw new MyCustomException("Erreur, payment card non existant");
            }
            while (resultSet.next()) {
                paymentCard = new PaymentCard().mapFromResultSet(resultSet);
            }
            log.info(String.format("payment card available with id: %d", paymentCard.getId()));
            return paymentCard;
        } catch (SQLException | MyCustomException e) {
            log.severe(e.getMessage());
        }
        return null;
    }


    @Override
    public ObservableList<PaymentCard> getAll() {
        ObservableList<PaymentCard> paymentCards = FXCollections.observableArrayList();
        String req = "select * from payment_card";
        try {
            PreparedStatement statement = connection.prepareStatement(req);
            ResultSet resultSet = statement.executeQuery(req);
            while (resultSet.next()) {
                PaymentCard paymentCard = new PaymentCard().mapFromResultSet(resultSet);
                paymentCards.add(paymentCard);
            }
            statement.close();
            resultSet.close();
        } catch (SQLException e) {
            log.severe(e.getMessage());
        }
        return paymentCards;
    }

    @Override
    public ObservableList<PaymentCard> getAllByUserId(Long id) {
        ObservableList<PaymentCard> paymentCards = FXCollections.observableArrayList();
        String req = "select * from payment_card where id_user=" + id + ";";
        try {
            PreparedStatement statement = connection.prepareStatement(req);
            ResultSet resultSet = statement.executeQuery(req);
            while (resultSet.next()) {
                PaymentCard paymentCard = new PaymentCard().mapFromResultSet(resultSet);
                paymentCards.add(paymentCard);
            }
            statement.close();
            resultSet.close();
        } catch (SQLException e) {
            log.severe(e.getMessage());
        }
        return paymentCards;
    }

    @Override
    public void updateEntity(PaymentCard paymentCard) {
        String req = "update payment_card set type = ? ,customer_id = ? , balance=?, id_user=? where id = ?";

        try {
            PreparedStatement statement = connection.prepareStatement(req);
            statement.setString(1, paymentCard.getType().toString());
            statement.setString(2, paymentCard.getCustomerId());
            statement.setDouble(3, paymentCard.getBalance());
            statement.setLong(4, paymentCard.getUserId());
            statement.setLong(5, paymentCard.getId());
            statement.executeUpdate();
            statement.close();
            log.info("payment card updated successfully");
        } catch (SQLException e) {
            log.severe(e.getMessage());
        }
    }

    @Override
    public void deleteEntityById(Long id) {
        String req = "delete from payment_card where id = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(req);
            statement.setLong(1, id);
            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            log.severe(e.getMessage());
        }
    }
}
