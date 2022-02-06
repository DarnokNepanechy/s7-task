package ru.dragulaxis.s7task.api;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.dragulaxis.s7task.entity.Role;
import ru.dragulaxis.s7task.entity.User;
import ru.dragulaxis.s7task.service.UserService;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api")
public class UserController {

    private final UserService userService;

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok().body(userService.getAllUsers());
    }

    @PostMapping("/user/save")
    public ResponseEntity<User> saveNewUser(@RequestBody User newUser) {
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("api/user/save").toUriString());
        return ResponseEntity.created(uri).body(userService.saveUser(newUser));
    }

    @PutMapping("/user/{id}")
    User replaceUser(@RequestBody User newUser, @PathVariable Long id) {
        return userService.editUser(newUser, id);
    }

    @DeleteMapping("/user/{id}")
    void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }

    @PostMapping("/role/save")
    public ResponseEntity<Role> saveNewRole(@RequestBody Role newRole) {
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("api/role/save").toUriString());
        return ResponseEntity.created(uri).body(userService.saveRole(newRole));
    }

    @PostMapping("/role/add-to-user")
    public ResponseEntity<?> addRoleToUser(@RequestBody RoleToUserForm form) {
        userService.addRoleToUser(form.getUsername(), form.getRoleName());
        return ResponseEntity.ok().build();
    }

    @Transactional
    @PutMapping("users/friends/{username}")
    public boolean addUserToFriends(@PathVariable String username, @RequestHeader("Authorization") String token) {
        User user = userService.getUserFromToken(token);
        User friend = userService.getUser(username);
        if (!username.equals(user.getUsername()) && friend != null) {
            return user.getFriends().add(friend);
        } else {
            return false;
        }
    }

    @Transactional
    @DeleteMapping("users/friends/{username}")
    public boolean deleteUserToFriends(@PathVariable String username, @RequestHeader("Authorization") String token) {
        User user = userService.getUserFromToken(token);
        User friend = userService.getUser(username);
        if (!username.equals(user.getUsername()) && friend != null) {
            return user.getFriends().remove(friend);
        } else {
            return false;
        }
    }

}

@Data
class RoleToUserForm {
    private String username;
    private String roleName;
}
