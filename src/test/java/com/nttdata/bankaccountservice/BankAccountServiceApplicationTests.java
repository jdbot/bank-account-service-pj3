package com.nttdata.bankaccountservice;

import com.nttdata.bankaccountservice.document.BankAccount;
import com.nttdata.bankaccountservice.service.BankAccountService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;

@AutoConfigureWebTestClient
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class BankAccountServiceApplicationTests {

	@Autowired
	private WebTestClient client;

	@Autowired
	private BankAccountService bankAccountService;

	@Test
	void findAllTest() {
		client.get()
				.uri("/bankAccount")
				.exchange()
				.expectStatus().isOk()
				.expectBodyList(BankAccount.class)
				.consumeWith(response ->{
					List<BankAccount> ba = response.getResponseBody();
					ba.forEach( b-> {
						System.out.println(b.getNumberAccount());
						Assertions.assertThat(ba.size()>0).isTrue();
					});

				});
	}

	@Test
	void findByIdTest() {
		BankAccount b = bankAccountService.findByCustomerId("63728b3f4cc7cc12bdc038da").blockFirst();
		client.get()
				.uri("/bankAccount/{id}", Collections.singletonMap("id", b.getId()))
				.exchange()
				.expectStatus().isOk()
				.expectBody(BankAccount.class)
				.consumeWith( response ->{
					BankAccount ba = response.getResponseBody();
					Assertions.assertThat(b.getNumberAccount().length()>0).isTrue();
				});
	}

	@Test
	void registerTest() {
		BankAccount ba = new BankAccount(null,"656567878",Float.parseFloat("1400"),"","63771520f2f20b0f9489203e",
				"636eea6e33ec63cafaf72fd2",0,5,2,"",false,"","2022-11-30");

		client.post()
				.uri("/bankAccount")
				.body(Mono.just(ba), BankAccount.class)
				.exchange()
				.expectStatus().isCreated()
				.expectBody(BankAccount.class)
				.consumeWith( response ->{
					BankAccount b = response.getResponseBody();
					Assertions.assertThat(b.getNumberAccount().equals("656567878")).isTrue();
				});
	}

	@Test
	void updateTest() {
		BankAccount ba = bankAccountService.findByNumberAccount("656567878").block();
		BankAccount bam = ba;
		bam.setTransactionLimit(3);
		client.put()
				.uri("/bankAccount/update")
				.body(Mono.just(bam), BankAccount.class)
				.exchange()
				.expectStatus().isOk()
				.expectBody(BankAccount.class)
				.consumeWith( response ->{
					BankAccount b = response.getResponseBody();
					Assertions.assertThat(b.getNumberAccount().equals("656567878")).isTrue();
					Assertions.assertThat(b.getTransactionLimit().equals(3)).isTrue();
				});
	}

	@Test
	void deleteTest() {
		BankAccount ba = bankAccountService.findByNumberAccount("656567878").block();

		client.delete()
				.uri("/bankAccount/{id}", Collections.singletonMap("id",ba.getId()))
				.exchange()
				.expectStatus().isOk()
				.expectBody();
	}


}
