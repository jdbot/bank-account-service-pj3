package com.nttdata.bankaccountservice.service;

import com.nttdata.bankaccountservice.document.AccountType;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Account Type service interface.
 */
public interface AccountTypeService {

    Flux<AccountType> findAll();

    Mono<AccountType> register(AccountType accountType);

    Mono<AccountType> update(AccountType accountType);

    Mono<AccountType> findById(String id);

    Mono<Void> delete(String id);

    Mono<Boolean> existsById(String id);


}
