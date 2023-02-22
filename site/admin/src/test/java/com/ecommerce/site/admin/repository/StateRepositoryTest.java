package com.ecommerce.site.admin.repository;

import static org.assertj.core.api.Assertions.assertThat;


import com.ecommerce.common.model.entity.Country;
import com.ecommerce.common.model.entity.State;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import java.util.List;
import java.util.Optional;


@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(value = false)
public class StateRepositoryTest {

	@Autowired
	private StateRepository repository;

	@Autowired
	private TestEntityManager entityManager;
	
	@Test
	public void testCreateStatesInIndia() {
		Integer countryId = 1;
		Country country = entityManager.find(Country.class, countryId);

		State state = repository.save(new State("West Bengal", country));
		
		assertThat(state).isNotNull();
		assertThat(state.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testCreateStatesInUS() {
		Country country = entityManager.find(Country.class, 2);

		State state = repository.save(new State("Washington", country));
		
		assertThat(state).isNotNull();
		assertThat(state.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testListStatesByCountry() {
		Country country = entityManager.find(Country.class, 2);
		List<State> stateList = repository.findByCountryOrderByNameAsc(country);
		
		stateList.forEach(System.out::println);
		
		assertThat(stateList.size()).isGreaterThan(0);
	}
	
	@Test
	public void testUpdateState() {
		String stateName = "Tamil Nadu";
		State state = repository.findById(3).get();
		
		state.setName(stateName);
		State updatedState = repository.save(state);
		
		assertThat(updatedState.getName()).isEqualTo(stateName);
	}
	
	@Test
	public void testGetState() {
		Optional<State> state = repository.findById(1);

		assertThat(state.isPresent());
	}
	
	@Test
	public void testDeleteState() {
		repository.deleteById(8);

		Optional<State> findById = repository.findById(8);

		assertThat(findById.isEmpty());		
	}

}
