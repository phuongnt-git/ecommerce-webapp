package com.ecommerce.site.admin.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;

import com.ecommerce.common.model.entity.Currency;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

/**
 * @author Nguyen Thanh Phuong
 */
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(value = false)
public class CurrencyRepositoryTest {

	@Autowired
	private CurrencyRepository repository;
	
	@Test
	public void testCreateCurrencies() {
		List<Currency> listCurrencies = Arrays.asList(
			new Currency("United States Dollar", "$", "USD"),
			new Currency("British Pound", "£", "GPB"),
			new Currency("Japanese Yen", "¥", "JPY"),
			new Currency("Euro", "€", "EUR"),
			new Currency("Russian Ruble", "₽", "RUB"),
			new Currency("South Korean Won", "₩", "KRW"),
			new Currency("Chinese Yuan", "¥", "CNY"),
			new Currency("Brazilian Real", "R$", "BRL"),
			new Currency("Australian Dollar", "$", "AUD"),
			new Currency("Canadian Dollar", "$", "CAD"),
			new Currency("Vietnamese Dong ", "₫", "VND"),
			new Currency("Indian Rupee", "₹", "INR")
		);
		
		repository.saveAll(listCurrencies);
		
		Iterable<Currency> iterable = repository.findAll();
		
		assertThat(iterable).size().isEqualTo(12);
	}
	
	@Test
	public void testListAllOrderByNameAsc() {
		List<Currency> currencyList = repository.findAllByOrderByNameAsc();
		
		currencyList.forEach(System.out::println);
		
		assertThat(currencyList.size()).isGreaterThan(0);
	}

}
