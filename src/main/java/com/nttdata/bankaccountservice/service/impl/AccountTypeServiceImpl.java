package com.nttdata.bankaccountservice.service.impl;

import com.nttdata.bankaccountservice.document.AccountType;
import com.nttdata.bankaccountservice.repository.AccountTypeRepository;
import com.nttdata.bankaccountservice.service.AccountTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Account Type Service Implementation.
 */
@Service
public class AccountTypeServiceImpl implements AccountTypeService {

    @Autowired
    private AccountTypeRepository accountTypeRepository;

    @Override
    public Flux<AccountType> findAll() {
        return this.accountTypeRepository.findAll();
    }

    @Override
    public Mono<AccountType> register(AccountType accountType) {
        return this.accountTypeRepository.save(accountType);
    }

    @Override
    public Mono<AccountType> update(AccountType accountType) {
        return this.accountTypeRepository.save(accountType);
    }

    @Override
    public Mono<AccountType> findById(String id) {
        return this.accountTypeRepository.findById(id);
    }

    @Override
    public Mono<Void> delete(String id) {
        return this.accountTypeRepository.deleteById(id);
    }

    @Override
    public Mono<Boolean> existsById(String id) {
        return this.accountTypeRepository.existsById(id);
    }
}
