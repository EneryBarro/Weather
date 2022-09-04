package com.sc.weatherapp.services;

import com.sc.weatherapp.exception.UserNotFoundException;
import com.sc.weatherapp.model.User;
import com.sc.weatherapp.repositories.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;


@Slf4j
@Service
@AllArgsConstructor
public class UserService {

    public static final String ERROR_TEMPLATE = "User with id: %s not found!";
    private UserRepository userRepository;


    public void createUser(User user) {
        log.info(user.toString());
        String id = UUID.randomUUID().toString();
        log.info("Create user: '{}'", id);
        user.setId(id);
        userRepository.save(user);
    }


    public User getUser(String id) {
        log.info("Get user: '{}'", id);
        return userRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("User with id: '{}' does not found", id);
                    throw new UserNotFoundException(String.format(ERROR_TEMPLATE, id));
                });
    }


    public void deleteUser(String id) {
        log.info("Delete user: '{}'", id);
        if (!userRepository.existsById(id)){
            log.error("User with id: '{}' does not exist", id);
            throw new UserNotFoundException(String.format(ERROR_TEMPLATE, id));
        }
        userRepository.deleteById(id);
    }


    public void updateUser(String id, User user) {
        log.info("Update user: '{}'", id);
        if (!userRepository.existsById(id)) {
            log.error("User with id: '{}' does not exist", id);
            throw new UserNotFoundException(String.format(ERROR_TEMPLATE, id));
        }

        User oldUser = getUser(id);

        userRepository.save(User.builder()
                .id(id)
                .login(user.getLogin() == null ? oldUser.getLogin() : user.getLogin())
                .firstName(user.getFirstName() == null ? oldUser.getFirstName() : user.getFirstName())
                .surname(user.getSurname() == null ? oldUser.getSurname() : user.getSurname())
                .email(user.getEmail() == null ? oldUser.getEmail() : user.getEmail())
                .phoneNumber(user.getPhoneNumber() == null ? oldUser.getPhoneNumber() : user.getPhoneNumber())
                .country(user.getCountry() == null ? oldUser.getCountry() : user.getCountry())
                .build()
        );
    }

}
