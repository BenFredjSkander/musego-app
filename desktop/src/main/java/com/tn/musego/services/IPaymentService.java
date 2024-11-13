package com.tn.musego.services;

import com.stripe.exception.StripeException;
import com.tn.musego.entities.PaymentCard;
import javafx.collections.ObservableList;

public interface IPaymentService {
    String createNewCustomer(String source, String email, String username) throws StripeException;

    void chargeNewCustomer(String customerId, long amount) throws StripeException;

    ObservableList<PaymentCard> getAllByUserId(Long id);
}
