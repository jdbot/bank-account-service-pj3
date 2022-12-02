package com.nttdata.bankaccountservice.document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@AllArgsConstructor
@Setter
@NoArgsConstructor
public class Transaction {
    //Date of the transaction
    private String transactionDate;
    //Amount of the transaction
    private float amount;
    //Transaction's type: Deposit or Withdrawl
    private String type;
    //Associated client's ID
    private String idClient;
    //Associated account's ID
    private String idAccount;
    //Associated account's amount after transaction
    private float accountAmount;
}
