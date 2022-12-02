package com.nttdata.bankaccountservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Transaction between accounts DTO.
 */
@Getter
@AllArgsConstructor
@Setter
@NoArgsConstructor
public class TransactionBetweenAccountsDto {

    private String transactionDate;
    private String senderAccountId;
    private String receptorAccountId;
    private float amount;
}
