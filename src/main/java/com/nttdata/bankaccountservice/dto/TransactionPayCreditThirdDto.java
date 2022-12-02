package com.nttdata.bankaccountservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Pay Credit Third DTO.
 */
@Getter
@AllArgsConstructor
@Setter
@NoArgsConstructor
public class TransactionPayCreditThirdDto {

    private String transactionDate;
    private String senderAccountId;
    private String receptorCreditId;
    private float amount;
}
