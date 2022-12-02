package com.nttdata.bankaccountservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@AllArgsConstructor
@Setter
@NoArgsConstructor
public class ClientDto {

    public String id;
    //Full name of the client
    private String name;
    //Email of the client
    private String email;
    //Phone of the client
    private String phone;
    //Document (DNI or RUC) of the client
    private String document;
    //Client's type: Person or Business
    private String type;
}
