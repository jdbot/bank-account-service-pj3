package com.nttdata.bankaccountservice.controller;

import com.nttdata.bankaccountservice.document.AccountType;
import com.nttdata.bankaccountservice.service.AccountTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Controller of Account Type.
 */
@RestController
@RequestMapping("/accountType")
public class AccountTypeController {

    @Autowired
    private AccountTypeService accountTypeService;

    //Method to get all the account type
    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public Flux<AccountType> findAll() {
        return accountTypeService.findAll();
    }

    //Method to insert new account type
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<AccountType> register(@RequestBody AccountType accountType) {
        return accountTypeService.register(accountType);
    }

    //Method to update account type
    @PutMapping("/update")
    @ResponseStatus(HttpStatus.OK)
    public Mono<AccountType> update(@RequestBody AccountType accountType) {
        return accountTypeService.update(accountType);
    }

    //Method to get account type by ID
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<AccountType> findById(@PathVariable("id") String id) {
        return accountTypeService.findById(id);
    }

    //Method to delete account type
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<Void> delete(@PathVariable("id") String id) {
        return accountTypeService.delete(id);
    }


}
