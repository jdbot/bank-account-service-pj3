package com.nttdata.bankaccountservice.document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Account Type Document.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "account-types")
public class AccountType {

    @Id
    private String id;
    private String code;
    private String name;
    private String isCommission;
    private String isLimit;
    private String limit;

}
