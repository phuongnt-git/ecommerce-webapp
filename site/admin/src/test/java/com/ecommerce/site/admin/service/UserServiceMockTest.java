package com.ecommerce.site.admin.service;

import com.ecommerce.common.model.entity.User;
import com.ecommerce.site.admin.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

/**
 * @author Nguyen Thanh Phuong
 */
@ExtendWith(MockitoExtension.class)
public class UserServiceMockTest {

    @Mock
    private UserRepository userRepository;

    private final LocalDateTime defaultLocalDateTime = LocalDateTime.of(2023, 1, 1, 12, 0);


    @Test
    void mockUserService() {
        System.out.println(LocalDateTime.now());
        System.out.println(userRepository.getClass());

        User user = new User()
                .toBuilder()
                .email("test@gmail.com")
                .firstName("test")
                .lastName("test")
                .password("test")
                .build();

        userRepository.save(user);

        Assertions.assertEquals(userRepository.findByEmail("test@gmail.com").getId(), 1);

        /*try (MockedStatic<LocalDateTime> mockedLocalDateTime = Mockito.mockStatic(LocalDateTime.class)) {

            mockedLocalDateTime.when(LocalDateTime::now).thenReturn(defaultLocalDateTime);

        }*/
    }
}
