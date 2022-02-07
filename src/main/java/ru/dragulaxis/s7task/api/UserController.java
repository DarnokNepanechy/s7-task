package ru.dragulaxis.s7task.api;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import ru.dragulaxis.s7task.entity.Role;
import ru.dragulaxis.s7task.entity.User;
import ru.dragulaxis.s7task.service.UserService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api")
public class UserController {

    private final UserService userService;

    // авторизация пользователя - ALL
    // по адресу api/login

    // регистрация нового пользователя - ALL
    @PostMapping
    public ResponseEntity<User> saveNewUser(User newUser) {
        return ResponseEntity.ok().body(userService.saveUser(newUser));
    }

    // получение списка имён всех пользователей - USER
    @GetMapping(value = {"/users/", "/users"})
    public ResponseEntity<List<String>> getAllUsersName() {
        return ResponseEntity.ok().body(
                userService.getAllUsers().stream()
                        .map(User::getUsername)
                        .toList()
        );
    }

    // получение списка имён пользователей по строке - USER
    @GetMapping("/users/{string}")
    public ResponseEntity<List<String>> getUsersByString(@PathVariable String string) {
        return ResponseEntity.ok().body(
                userService.getAllUsers().stream()
                        .map(User::getUsername)
                        .filter(username -> username.toLowerCase().contains(string.toLowerCase()))
                        .toList()
        );
    }

    // добавление пользователя в друзья - USER
    @Transactional
    @PutMapping("/user/friends/{username}")
    public boolean addUserToFriends(@PathVariable String username, @RequestHeader("Authorization") String token) {
        User user = userService.getUserFromToken(token);
        User friend = userService.getUser(username);
        if (!username.equals(user.getUsername()) && friend != null) {
            return user.getFriends().add(friend);
        } else {
            return false;
        }
    }

    // удаление пользователя из друзей - USER
    @Transactional
    @DeleteMapping("/user/friends/{username}")
    public boolean deleteUserToFriends(@PathVariable String username, @RequestHeader("Authorization") String token) {
        User user = userService.getUserFromToken(token);
        User friend = userService.getUser(username);
        if (!username.equals(user.getUsername()) && friend != null) {
            return user.getFriends().remove(friend);
        } else {
            return false;
        }
    }

    // получение списка своих друзей - USER
    @GetMapping("/user/friends")
    public List<String> getAllFriends(@RequestHeader("Authorization") String token) {
        return userService.getUserFromToken(token).getFriends().stream().map(User::getUsername).toList();
    }

    // получение списка пользователей со всей информацией - ADMIN
    @GetMapping("/admin/users")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok().body(userService.getAllUsers());
    }

    // редактирование пользователя - ADMIN
    @PutMapping("/admin/user/{id}")
    User replaceUser(@RequestBody User newUser, @PathVariable Long id) {
        return userService.editUser(newUser, id);
    }

    // удаление пользователя - ADMIN
    @DeleteMapping("/admin/user/{id}")
    void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }

    // добавление роли - ADMIN
    @PostMapping("/admin/role/save")
    public ResponseEntity<Role> saveNewRole(@RequestBody Role newRole) {
        return ResponseEntity.ok().body(userService.saveRole(newRole));
    }

    // добавление роли к пользователю - ADMIN
    @PostMapping("/admin/role/add-to-user")
    public ResponseEntity<?> addRoleToUser(@RequestBody RoleToUserForm form) {
        userService.addRoleToUser(form.getUsername(), form.getRoleName());
        return ResponseEntity.ok().build();
    }
}

@Data
class RoleToUserForm {
    private String username;
    private String roleName;
}
