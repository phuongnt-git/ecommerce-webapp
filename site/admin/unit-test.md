# Unit Test and Actuator
## Unit Test for site admin
### Add dependencies
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

### Source Test Structure
```
├── <b>src</b>
│   ├── <b>test</b>
│   │   ├── <b>java</b>
│   │   │   ├── <b>com.ecommerce.site.admin</b>
│   │   │   │   ├── controller
│   │   │   │   ├── repository
│   │   │   │   ├── service
```

### Result
#### Test `testEncodePassword`
<img src="../result/test-encode-password.png" alt="">

#### Test in Repository layer
* Test `testSearchUser` method in `UserRepositoryTest` 
(<a href="src/test/java/com/ecommerce/site/admin/repository/UserRepositoryTest.java">Click here</a> for detail)
<img src="../result/test-search-user.png" alt="">

#### Test in Service layer
##### Using `@SpringBootTest` and `@TestPropertySource` to get TEST database connection from `application-test.properties`
This demo result from `UserServiceTest`
(<a href="src/test/java/com/ecommerce/site/admin/repository/UserServiceTest.java">Click here</a> for detail).
Before each and after each unit test, `JdbcTemplate` will execute sql script to get data for testing 
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

* Test `testDeleteUser`
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
and its result
<img src="../result/test-delete-user.png" alt="">

#### Using