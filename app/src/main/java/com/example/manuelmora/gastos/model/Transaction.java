package com.example.manuelmora.gastos.model;

import java.util.UUID;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * @author Juan Mora
 * @since 11/09/2018 13:18
 *
 * POJO class to store all user transactions.
 */

public class Transaction extends RealmObject {

    public static final String DEBT_TYPE = "debt";
    public static final String PAYMENT_TYPE = "payment";

    @PrimaryKey
    public String id;
    public String type;
    public String concept;
    public double amount;

    // No params constructor
    public Transaction() {
        // Generate a random ID
        this.id = UUID.randomUUID().toString();
        this.type = "";
        this.concept = "";
        this.amount = 0.0;
    }

    public Transaction(String type, String concept, double amount) {
        // Generate a random ID
        id = UUID.randomUUID().toString();
        this.type = type;
        this.concept = concept;
        this.amount = amount;
    }
}
