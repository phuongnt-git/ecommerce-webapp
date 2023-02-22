package com.ecommerce.site.admin.repository;

import static org.assertj.core.api.Assertions.assertThat;


import com.ecommerce.common.model.entity.Country;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.List;
import java.util.Optional;


@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(value = false)
public class CountryRepositoryTests {
	
	@Autowired
	private CountryRepository repository;
	
	@Test
	public void testCreateCountry() {
		Country country = repository.save(new Country("China", "CN"));
		assertThat(country).isNotNull();
		assertThat(country.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testListCountries() {
		List<Country> countryList = repository.findAllByOrderByNameAsc();
		countryList.forEach(System.out::println);
		
		assertThat(countryList.size()).isGreaterThan(0);
	}

	@Test
	public void testUpdateCountry() {
		Integer id = 1;
		String name = "Republic of India";
		
		Country country = repository.findById(id).get();
		country.setName(name);
		
		Country updatedCountry = repository.save(country);
		
		assertThat(updatedCountry.getName()).isEqualTo(name);
	}
	
	@Test
	public void testGetCountry() {
		Country country = repository.findById(3).get();

		assertThat(country).isNotNull();
	}
	
	@Test
	public void testDeleteCountry() {
		repository.deleteById(5);
		
		Optional<Country> findById = repository.findById(5);

		assertThat(findById.isEmpty());
	}
}
