package com.tn.musego.entities;

import com.tn.musego.entities.enums.TypePaymentCard;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.ResultSet;
import java.sql.SQLException;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentCard {
    private Long id;
    private String customerId;
    private TypePaymentCard type;
    private Double balance;
    private Long userId;

    public PaymentCard mapFromResultSet(ResultSet resultSet) throws SQLException {
        return new PaymentCard(
                resultSet.getLong("id"),
                resultSet.getString("customer_id"),
                Enum.valueOf(TypePaymentCard.class, resultSet.getString("type")),
                resultSet.getDouble("balance"),
                resultSet.getLong("id_user")
        );
    }
}
