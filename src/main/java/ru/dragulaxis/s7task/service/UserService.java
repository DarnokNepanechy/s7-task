package ru.dragulaxis.s7task.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.dragulaxis.s7task.entity.User;
import ru.dragulaxis.s7task.repository.UserRepository;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User editUser(User newUser, Long id) {
        User user = userRepository.findById(id).orElse(null);

        if (user != null) {
            if (newUser.getUsername() != null) {
                user.setUsername(newUser.getUsername());
            }
            if (newUser.getPassword() != null) {
                user.setPassword(newUser.getPassword());
            }

            return userRepository.save(user);
        } else {
            System.out.println("Пользователя с ID = " + id + " не существует");
            return null;
        }
    }

    public void deleteUser(Long id) {
        User user = (userRepository.findById(id).orElse(null));
        if (user != null) {
            userRepository.delete(user);
        } else {
            System.out.println("Пользователя с ID = " + id + " не существует");
        }
    }
}
