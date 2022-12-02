package com.nttdata.bankaccountservice.service.impl;

import com.nttdata.bankaccountservice.document.BankAccount;
import com.nttdata.bankaccountservice.dto.*;
import com.nttdata.bankaccountservice.document.Transaction;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import com.nttdata.bankaccountservice.repository.BankAccountRepository;
import com.nttdata.bankaccountservice.service.AccountTypeService;
import com.nttdata.bankaccountservice.service.BankAccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * Bank Account Service Implementation.
 */
@Service
public class BankAccountServiceImpl implements BankAccountService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BankAccountServiceImpl.class);

    @Autowired
    private WebClient.Builder webClient;

    @Autowired
    private BankAccountRepository bankAccountRepository;

    @Autowired
    private AccountTypeService accountTypeService;

    @Override
    public Flux<BankAccount> findAll() {
        return this.bankAccountRepository.findAll();
    }

    @Override
    public Mono<BankAccount> register(BankAccount bankAccount) {
        return this.bankAccountRepository.save(bankAccount);
    }

    @Override
    public Mono<BankAccount> update(BankAccount bankAccount) {
        return this.bankAccountRepository.save(bankAccount);
    }

    @Override
    public Mono<BankAccount> findById(String id) {
        return this.bankAccountRepository.findById(id);
    }

    @Override
    public Mono<Void> delete(String id) {
        return bankAccountRepository.deleteById(id);
    }

    @Override
    public Mono<Boolean> existsById(String id) {
        return this.bankAccountRepository.existsById(id);
    }

    @Override
    public Mono<ClientDto> findClientById(String clientId) {
        LOGGER.info("Consulted client from the bank-account-service");
        Mono<ClientDto> client = this.webClient.build().get().uri("/client/{id}", clientId)
                .retrieve().bodyToMono(ClientDto.class);
//                .delayElement(Duration.ofSeconds(5)); // testing the timeout de 2s
        return client;
    }

    @Override
    public Mono<BankAccount> validateRegister(BankAccount bankAccount) {
        return this.webClient.build().get().uri("/client/{id}", bankAccount.getCustomerId())
            .retrieve()
            .bodyToMono(ClientDto.class)
            .flatMap(dc -> {
                if (dc.getType().equals("business")) {
                    return this.accountTypeService.findById(bankAccount.getType()).filter(obj ->
                        obj.getCode().equals("3"))
                        .flatMap(x-> register(bankAccount));
                } else {
                    return findClientHasDebt(bankAccount.getCustomerId()).flatMap(x->{
                        LOGGER.info(x);
                        if (x.equals("1")) {
                            throw new RuntimeException("Client has overdue debt");
                        } else {
                            return register(bankAccount);
                        }
                    }).switchIfEmpty(register(bankAccount));
                }
            });
    }

    @Override
    public Flux<BankAccount> findByCustomerIdAndType(String customerId, String type){
        return this.bankAccountRepository.findByCustomerIdAndType(
                customerId, type);
    }

    public Mono<BankAccount> validateBankAccount(String customerId, String type){
        LOGGER.info("validateBankAccount");
        return this.bankAccountRepository.findByCustomerIdAndType(
            customerId, type)
            .next();
    }

    public Mono<BankAccount> doDeposit(Transaction transaction) {
        return findById(transaction.getIdAccount()).flatMap(x -> {
            float newAmount = x.getAmount() + transaction.getAmount();
            transaction.setType("deposit");
            transaction.setIdClient(x.getCustomerId());
            transaction.setAccountAmount(newAmount);
            x.setAmount(newAmount);
            x.setNumberOfTransactions(x.getNumberOfTransactions()+1);
            return this.webClient.build().post().uri("/transaction/").
                    header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE).
                    body(Mono.just(transaction), Transaction.class).
                    retrieve().
                    bodyToMono(Transaction.class).
                    flatMap(y -> update(x));
        });
    }

    @Override
    public Mono<BankAccount> doWithdrawl(Transaction transaction) {
        return findById(transaction.getIdAccount()).flatMap(x -> {
            float newAmount = x.getAmount() - transaction.getAmount();
            if( newAmount >= 0) {
                transaction.setType("withdrawl");
                transaction.setIdClient(x.getCustomerId());
                transaction.setAccountAmount(newAmount);
                x.setAmount(newAmount);
                x.setNumberOfTransactions(x.getNumberOfTransactions()+1);
                return this.webClient.build().post().uri("/transaction/").
                        header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE).
                        body(Mono.just(transaction), Transaction.class).
                        retrieve().
                        bodyToMono(Transaction.class).
                        flatMap(y -> update(x).flatMap(z->doCommission(transaction)));
            }else{
                return Mono.empty();
            }
        });
    }
    @Override
    public Mono<BankAccount> doTransactionBetweenAccounts(TransactionBetweenAccountsDto t) {
        Transaction tSender = new Transaction(t.getTransactionDate(), t.getAmount(), "withdrawl",null, t.getSenderAccountId(), 0);
        Transaction tReceptor = new Transaction(t.getTransactionDate(), t.getAmount(), "deposit", null, t.getReceptorAccountId(), 0);
        return doWithdrawl(tSender).flatMap(x -> doDeposit(tReceptor));
    }
    @Override
    public Mono<BankAccount> doCommission(Transaction transaction) {
        return findById(transaction.getIdAccount()).flatMap(x -> {
            if( x.getNumberOfTransactions() + 1 > x.getTransactionLimit()) {
                float newAmount = x.getAmount() - x.getCommission();
                transaction.setType("commission");
                transaction.setIdClient(x.getCustomerId());
                transaction.setAccountAmount(newAmount);
                return this.webClient.build().post().uri("/transaction/").
                    header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE).
                    body(Mono.just(transaction), Transaction.class).
                    retrieve().
                    bodyToMono(Transaction.class).
                    flatMap(y -> update(x));
            } else {
                return Mono.empty();
            }
        });
    }

    @Override
    public Flux<BankAccount> findAccountsByDebitCard(String debitCardId) {
        return this.bankAccountRepository.findByDebitCardId(debitCardId);
    }

    @Override
    public Mono<BankAccount> associateToDebitCard(String bankAccountId, String debitCardId) {
        return findById(bankAccountId).map(account -> {
            account.setDebitCardId(debitCardId);
            account.setAssociationDate(LocalDateTime.now().toString());
            account.setPrimaryAccount(false);
            return account;
        }).flatMap(account -> update(account));
    }

    @Override
    public Mono<BankAccount> makePrimaryAccount(String bankAccountId) {
        return findById(bankAccountId).map(account -> {
            account.setPrimaryAccount(true);
            return account;
        }).flatMap(account -> update(account));
    }

    public Flux<BankAccount> findByCustomerId(String customerId) {
        LOGGER.info("findByCustomerId");
        return this.bankAccountRepository.findByCustomerId(
                        customerId);
    }

    @Override
    public Mono<String> findClientHasDebt(String clientId) {
        return this.webClient.build().get().uri("/bankDebt/debtByCustomerId/{id}", clientId)
                .retrieve().bodyToFlux(BankDebtDto.class).next()
                .flatMap(x->Mono.just("1"));
    }
    @Override
    public Mono<BankCreditDto> doPayCreditThird(TransactionPayCreditThirdDto t) {
        Transaction tSender = new Transaction(t.getTransactionDate(), t.getAmount(), "withdrawl",null, t.getSenderAccountId(), 0);
        Transaction tReceptor = new Transaction(t.getTransactionDate(), t.getAmount(), "credit payment", null, t.getReceptorCreditId(), 0);
        return doWithdrawl(tSender).flatMap(x -> {
            return this.webClient.build().post().uri("/bankCredit/paycredit").bodyValue(tReceptor)
                    .retrieve().bodyToFlux(BankCreditDto.class).next();
        });
    }
}
