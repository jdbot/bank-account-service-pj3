package com.nttdata.bankaccountservice.repository;

import com.nttdata.bankaccountservice.document.AccountType;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Account Type Repository.
 */
@Repository
public interface AccountTypeRepository extends ReactiveMongoRepository<AccountType,String> {

}
