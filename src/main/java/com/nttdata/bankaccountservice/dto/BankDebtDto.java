package com.nttdata.bankaccountservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BankDebtDto {

    @Id
    private String id;
    //amount of the banking Debt
    private Float amount;
    //amount of the banking Debt
    private Float balance;
    //end date of the banking Debt
    private String paymentDate;
    //id of the client
    private String customerId;
    //id of the credit
    private String creditId;

}
