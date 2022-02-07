package ru.dragulaxis.s7task.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.dragulaxis.s7task.config.SecurityConfig;
import ru.dragulaxis.s7task.entity.Role;
import ru.dragulaxis.s7task.entity.User;
import ru.dragulaxis.s7task.repository.RoleRepository;
import ru.dragulaxis.s7task.repository.UserRepository;

import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found in the database");
        }

        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        user.getRoles().forEach(role -> {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        });
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), authorities);
    }

    public User saveUser(User user) {
        if (
                user.getUsername().matches("^[a-zA-Z0-9]+$") &&
                user.getUsername().replaceAll("\\s+","").length() > 2 &&
                user.getPassword().matches("^[a-zA-Z0-9]+$") &&
                user.getUsername().replaceAll("\\s+","").length() > 2
        ) {
            // кодирование пароля
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            // добавление роли user по умолчанию
            HashSet<Role> roles = new HashSet<>();
            roles.add(roleRepository.findByName("ROLE_USER"));
            user.setRoles(roles);
            return userRepository.save(user);
        } else {
            return null;
        }

    }

    public Role saveRole(Role role) {
        return roleRepository.save(role);
    }

    public void addRoleToUser(String username, String roleName) {
        User user = userRepository.findByUsername(username);
        Role role = roleRepository.findByName(roleName);
        user.getRoles().add(role);
    }

    public User getUser(String name) {
        return userRepository.findByUsername(name);
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

    public User getUserFromToken(String token) {
        Algorithm algorithm = Algorithm.HMAC256(SecurityConfig.SECRET.getBytes());
        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT decodedJWT = verifier.verify(token);
        String username = decodedJWT.getSubject();

        return userRepository.findByUsername(username);
    }
}
