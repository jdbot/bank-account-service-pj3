package com.nttdata.bankaccountservice.service;

import com.nttdata.bankaccountservice.document.BankAccount;
import com.nttdata.bankaccountservice.dto.*;
import com.nttdata.bankaccountservice.document.Transaction;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Bank Account service interface.
 */
public interface BankAccountService {

    Flux<BankAccount> findAll();

    Mono<BankAccount> register(BankAccount bankAccount);

    Mono<BankAccount> update(BankAccount bankAccount);

    Mono<BankAccount> findById(String id);

    Mono<Void> delete(String id);

    Mono<Boolean> existsById(String id);

    Mono<ClientDto> findClientById(String clientId);

    Flux<BankAccount> findByCustomerIdAndType(String customerId, String type);

    Mono<BankAccount> validateRegister(BankAccount bankAccount);

    Mono<BankAccount> doDeposit(Transaction transaction);

    Mono<BankAccount> doWithdrawl(Transaction transaction);

    Mono<BankAccount> doTransactionBetweenAccounts(TransactionBetweenAccountsDto t);

    Mono<BankAccount> doCommission(Transaction transaction);

    Flux<BankAccount> findAccountsByDebitCard(String debitCardId);

    Mono<BankAccount> associateToDebitCard(String bankAccountId, String debitCardId);

    Mono<BankAccount> makePrimaryAccount(String bankAccountId);

    Flux<BankAccount> findByCustomerId(String customerId);

    Mono<String> findClientHasDebt(String clientId);

    Mono<BankCreditDto> doPayCreditThird(TransactionPayCreditThirdDto t);
}
