package com.nttdata.bankaccountservice.service.consumer;

import com.nttdata.bankaccountservice.repository.BankAccountRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class Consumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(Consumer.class);

    @Autowired
    private BankAccountRepository bankAccountRepository;

    @KafkaListener(topics = "primary-account" , groupId = "default")
    public void makePrimaryAccountKafka(String message) {
        LOGGER.info("consumiendo mensaje " + message.trim());
        bankAccountRepository.findById(message).subscribe(account -> {
            LOGGER.info("found account " + account);
            account.setPrimaryAccount(true);
            bankAccountRepository.save(account).subscribe();
        });
    }
}
