package com.sc.weatherapp.services;

import com.sc.weatherapp.exception.UserNotFoundException;
import com.sc.weatherapp.model.User;
import com.sc.weatherapp.repositories.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import java.util.Optional;

public class UserServiceTest {

    private static UserService userService;
    @MockBean
    private static UserRepository userRepository;


    @BeforeAll
    static void setup() {
        userRepository = Mockito.mock(UserRepository.class);
        userService = new UserService(userRepository);
    }


    @Test
    void createUser_success() {
        User sourceUser = User.builder()
                .id("1")
                .login("login")
                .firstName("firstname")
                .surname("surname")
                .email("email@gmail.com")
                .phoneNumber("+380phoneNumber")
                .country("Ukraine")
                .build();

        userService.createUser(sourceUser);

        Mockito.verify(userRepository, Mockito.times(1))
                .save(Mockito.eq(sourceUser));
    }


    @Test
    void getUser_success() {
        User sourceUser = User.builder()
                .id("1")
                .login("login")
                .firstName("firstname")
                .surname("surname")
                .email("email@gmail.com")
                .phoneNumber("+380phoneNumber")
                .country("Ukraine")
                .build();
        String id = "1";

        Mockito.doReturn(Optional.of(sourceUser))
                .when(userRepository)
                .findById(id);

        User actualUser = userService.getUser(id);

        User expectedUser = User.builder()
                .id("1")
                .login("login")
                .firstName("firstname")
                .surname("surname")
                .email("email@gmail.com")
                .phoneNumber("+380phoneNumber")
                .country("Ukraine")
                .build();

        Assertions.assertEquals(actualUser, expectedUser);
    }

    @Test
    void getUser_failure() {
        String id = "1";

        Mockito.doReturn(Optional.empty())
                .when(userRepository)
                .findById(Mockito.eq(id));

        String expectedMessage = "User with id: 1 not found!";

        String actualMessage = Assertions.assertThrows(UserNotFoundException.class, () ->
                userService.getUser(id)).getMessage();

        Assertions.assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void updateUser_success() {
        User sourceUser = User.builder()
                .id("1")
                .login("login")
                .firstName("firstname")
                .surname("surname")
                .email("email@gmail.com")
                .phoneNumber("+380phoneNumber")
                .country("Ukraine")
                .build();
        String id = "1";

        Mockito.doReturn(true)
                .when(userRepository).existsById(Mockito.eq("1"));
        Mockito.doReturn(Optional.of(sourceUser))
                .when(userRepository).findById(Mockito.eq("1"));

        userService.updateUser(id, sourceUser);

        User expectedUser = User.builder()
                .id("1")
                .login("login")
                .firstName("firstname")
                .surname("surname")
                .email("email@gmail.com")
                .phoneNumber("+380phoneNumber")
                .country("Ukraine")
                .build();

        Mockito.verify(userRepository, Mockito.times(1))
                .save(Mockito.eq(expectedUser));
    }

    @Test
    void updateUser_failure() {
        String id = "1";
        User sourceUser = User.builder()
                .id("1")
                .login("login")
                .firstName("firstname")
                .surname("surname")
                .email("email@gmail.com")
                .phoneNumber("+380phoneNumber")
                .country("Ukraine")
                .build();

        Mockito.doReturn(false)
                .when(userRepository)
                .existsById(Mockito.eq(id));

        String expectedMessage = "User with id: 1 not found!";

        String actualMessage = Assertions.assertThrows(UserNotFoundException.class, () ->
                userService.updateUser(id, sourceUser)).getMessage();

        Assertions.assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void deleteUser_success() {
        String id = "1";

        Mockito.doReturn(true)
                .when(userRepository)
                .existsById(Mockito.eq(id));

        userService.deleteUser(id);

        Mockito.verify(userRepository, Mockito.times(1)).deleteById(Mockito.eq(id));
    }

    @Test
    void deleteUser_failure() {
        String id = "1";

        Mockito.doReturn(false)
                .when(userRepository)
                .existsById(Mockito.eq(id));

        String expectedMessage = "User with id: 1 not found!";

        String actualMessage = Assertions.assertThrows(UserNotFoundException.class, () ->
                userService.deleteUser(id)).getMessage();

        Assertions.assertEquals(expectedMessage, actualMessage);
    }

}
