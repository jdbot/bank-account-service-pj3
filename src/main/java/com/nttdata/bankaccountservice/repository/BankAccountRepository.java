package com.nttdata.bankaccountservice.repository;

import com.nttdata.bankaccountservice.document.BankAccount;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

/**
 * Bank Account Repository.
 */
@Repository
public interface BankAccountRepository extends ReactiveMongoRepository<BankAccount, String> {

    Flux<BankAccount> findByCustomerIdAndType(String customerId, String type);

    Flux<BankAccount> findByDebitCardId(String debitCardId);

    Flux<BankAccount> findByCustomerId(String customerId);
}
