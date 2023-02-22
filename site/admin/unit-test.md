# Unit Testing with JUnit and Mockito
## Dependencies
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-test</artifactId>
    <scope>test</scope>
    <exclusions>
        <exclusion>
            <groupId>junit</groupId>
            <artifactId>junit</artifactId>
        </exclusion>
    </exclusions>
</dependency>
<dependency>
    <groupId>org.junit.jupiter</groupId>
    <artifactId>junit-jupiter-api</artifactId>
    <version>5.9.2</version>
    <scope>test</scope>
</dependency>
<dependency>
    <groupId>org.junit.jupiter</groupId>
    <artifactId>junit-jupiter-engine</artifactId>
    <version>5.9.2</version>
    <scope>test</scope>
</dependency>
```

## Test Structure
```
├── <b>src</b>
│   ├── <b>test</b>
│   │   ├── <b>java</b>
│   │   │   ├── <b>com.ecommerce.site.admin</b>
│   │   │   │   ├── controller
│   │   │   │   ├── repository
│   │   │   │   ├── rest
│   │   │   │   ├── service
```

## Overview
<a href="src/test/java/com/ecommerce/site/admin">Click here</a> to view all layer

For code detail of specific test classes in each layer:
* <a href="src/test/java/com/ecommerce/site/admin/controller">Controller</a>
* <a href="src/test/java/com/ecommerce/site/admin/repository">Repository</a>
* <a href="src/test/java/com/ecommerce/site/admin/rest">Rest Controller</a>
* <a href="src/test/java/com/ecommerce/site/admin/service">Service</a>


### 1. Unit Testing In Repository
* `testSearchUser` case in `UserRepositoryTest` and its result (<a href="src/test/java/com/ecommerce/site/admin/repository/UserRepositoryTest.java">Click here</a> to view code)
```java
@Test
public void testSearchUser() {
    String keyword = "gmail";

    int pageNumber = 0;
    int pageSize = 5;

    Pageable pageable = PageRequest.of(pageNumber, pageSize);
    Page<User> page = repository.findAll(keyword, pageable);

    List<User> listUsers = page.getContent();

    listUsers.forEach(System.out::println);

    assertThat(listUsers.size()).isGreaterThan(0);
}
```

<img src="../result/test-search-user.png" alt="">

### 2. Unit Testing In Service
#### a) Using `@SpringBootTest` and `@TestPropertySource` to test in TEST database connection from `application-test.properties`
```java
@TestPropertySource(locations = "/application-test.properties")
@SpringBootTest
```

###### This demo below from `UserServiceTest` (<a href="src/test/java/com/ecommerce/site/admin/service/UserServiceTest.java">Click here</a> for detail)
Before each & after each unit testing, `JdbcTemplate` will execute sql script to get data for testing 
and delete all after finish each unit test.
```java
@Autowired
private JdbcTemplate jdbc;

@Value("${sql.script.insert.user}")
private String sqlInsertUser;

@Value("${sql.script.delete.user}")
private String sqlDeleteUser;

@Value("${sql.script.delete.user-role}")
private String sqlDeleteUserRole;

@Value("${sql.script.delete.role}")
private String sqlDeleteRole;

@BeforeEach
public void setupDatabase() {
    jdbc.execute(sqlInsertUser);
}

@AfterEach
public void setupAfterTransaction() {
    jdbc.execute(sqlDeleteUserRole);
    jdbc.execute(sqlDeleteUser);
    jdbc.execute(sqlDeleteRole);
}
```

* `testDeleteUser` case and its result
```java
@Test
@Rollback
public void testDeleteUser() throws UserNotFoundException {
    Optional<User> user = userRepository.findById(1);
    Assertions.assertTrue(user.isPresent(), "return true");

    userService.deleteById(1);

    user = userRepository.findById(1);
    Assertions.assertFalse(user.isPresent(), "return false");
}
```

<img src="../result/test-delete-user.png" alt="">

#### b) Using `Mockito` with annotation `@ExtendWith(MockitoExtension.class)` and `@ExtendWith(SpringExtension.class)` to test in DEFAULT database connection from `application.properties`
###### This demo below from `CategoryServiceTest` (<a href="src/test/java/com/ecommerce/site/admin/service/CategoryServiceTest.java">Click here</a> to view code)

* Using `@MockBean` and `@InjectsMock`:
```java
@MockBean
private CategoryRepository repository;

@InjectMocks
private CategoryService service;
```

* `testCheckUniqueInNewModeReturnDuplicateName` case and its result
```java
@Test
public void testCheckUniqueInNewModeReturnDuplicateName() {
    String name = "Computers";
    String alias = "abc";
    
    Category category = new Category(null, name, alias);
    
    Mockito.when(repository.findByName(name)).thenReturn(category);
    Mockito.when(repository.findByAlias(alias)).thenReturn(null);
    
    String result = service.checkUnique(null, name, alias);
    
    assertThat(result).isEqualTo("DuplicateName");
}
```
<img src="../result/test-check-category-unique.png" alt="">

### 3. Unit Testing In Controller
#### For unit testing in `@Controller` & `@RestController` layer, set ignore filter through annotation
```java
@AutoConfigureMockMvc(addFilters = false)
```

###### This demo below from `UserControllerTest` (<a href="src/test/java/com/ecommerce/site/admin/controller/UserControllerTest.java">Click here</a> to view code)
* `testSaveUserReturnBadRequest` case and its result
```java
@Test
public void testSaveUserReturnBadRequest () throws Exception {
    MockMultipartFile mockMultipartFile = new MockMultipartFile(
            "image",
            "test.jpg",
            "image/jpeg",
            new byte[0]
    );

    MvcResult mvcResult = this.mockMvc
            .perform(MockMvcRequestBuilders.multipart("/users/save")
                    .param("image", mockMultipartFile.getContentType())
                    .param("email", "test2@test.com")
                    .param("firstName", "test2")
                    .param("lastName", "test2")
                    .param("password", "test2"))
            .andExpect(MockMvcResultMatchers.status().isBadRequest())
            .andReturn();

    ModelAndView modelAndView = mvcResult.getModelAndView();

    if (modelAndView != null) {
        ModelAndViewAssert.assertViewName(modelAndView, "users");
    }
}
```

<img src="../result/test-save-user-return-bad-request.png" alt="">

###### This demo below from `StateRestControllerTest` (<a href="src/test/java/com/ecommerce/site/admin/controller/UserControllerTest.java">Click here</a> to view code)
* `testCreateState` case and its result
```java
@Test
@WithMockUser(username = "something", password = "something", roles = "Admin")
public void testCreateState() throws Exception {
    String url = "/states/save";
    Integer countryId = 2;
    Country country = countryRepository.findById(countryId).get();
    State state = new State("Arizona", country);
    
    MvcResult result = mockMvc.perform(post(url).contentType("application/json")
            .content(objectMapper.writeValueAsString(state))
            .with(csrf()))
        .andDo(print())
        .andExpect(status().isOk())
        .andReturn();
    
    String response = result.getResponse().getContentAsString();
    Integer stateId = Integer.parseInt(response);
    Optional<State> findById = stateRepository.findById(stateId);
    
    assertThat(findById.isPresent());		
}
```

<img src="../result/test-create-state.png" alt="">

### Other Unit Testing
* `testEncodePassword` case and its result
```java
@Test
public void testEncodePassword() {
    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    String password = "test";
    String encodedPassword = passwordEncoder.encode(password);

    System.out.println(encodedPassword);

    boolean matches = passwordEncoder.matches(password, encodedPassword);

    assertThat(matches).isTrue();
}
```

<img src="../result/test-encode-password.png" alt="">

# Spring Boot Actuator
## Overview
### Config properties in application
```properties
management.server.port=8090
management.endpoints.web.exposure.include=*
management.endpoint.shutdown.enabled=true
```

### Access Actuator using Postman
<img src="../result/access-actuator.png" alt="">

### Shutdown application through Actuator
<img src="../result/shutdown-application.png" alt="">