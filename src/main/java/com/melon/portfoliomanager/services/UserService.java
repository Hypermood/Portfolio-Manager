package com.melon.portfoliomanager.services;

import com.melon.portfoliomanager.dtos.UserDeleteDto;
import com.melon.portfoliomanager.dtos.UserDto;
import com.melon.portfoliomanager.exceptions.NoSuchUserException;
import com.melon.portfoliomanager.exceptions.UsernameAlreadyUsedException;
import com.melon.portfoliomanager.models.User;
import com.melon.portfoliomanager.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createUser(User user) {

        if (!isUsernameFree(user)) {
            throw new UsernameAlreadyUsedException(user.getUsername());
        }
        return userRepository.save(user);
    }

    public User transformDto(UserDto userDto) {
        return new User(userDto.getUsername(), userDto.getEmail(), userDto.getFirstName(), userDto.getLastName());
    }

    public boolean isUsernameFree(User user) {
        return userRepository.findByUsername(user.getUsername()).isEmpty();
    }

    public void deleteUser(UserDeleteDto userDeleteDto) {

        long res = userRepository.deleteByUsername(userDeleteDto.getUsername());

        if (res == 0) {
            throw new NoSuchUserException("There is no such user in the database.");
        }

    }

}
