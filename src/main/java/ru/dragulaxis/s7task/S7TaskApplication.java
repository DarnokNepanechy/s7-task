package ru.dragulaxis.s7task;

import org.h2.tools.Server;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.dragulaxis.s7task.entity.Role;
import ru.dragulaxis.s7task.entity.User;
import ru.dragulaxis.s7task.service.UserService;

import java.sql.SQLException;
import java.util.HashSet;

@SpringBootApplication
public class S7TaskApplication {

	public static void main(String[] args) {
		SpringApplication.run(S7TaskApplication.class, args);
	}

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	// Этот метод запускается после инициализации приложения - тут первоночальные данные для тестов
	@Bean
	CommandLineRunner run(UserService userService) {
		return  args -> {
			userService.saveRole(new Role(null, "ROLE_ADMIN"));
			userService.saveRole(new Role(null, "ROLE_USER"));

			userService.saveUser(new User(null, "KanyeWest", "god", new HashSet<>(), new HashSet<>()));
			userService.saveUser(new User(null, "WillSmith", "1234", new HashSet<>(), new HashSet<>()));
			userService.saveUser(new User(null, "JohnSnow", "1234", new HashSet<>(), new HashSet<>()));
			userService.saveUser(new User(null, "JohnTravolta", "vincent", new HashSet<>(), new HashSet<>()));

			// admin
			userService.addRoleToUser("KanyeWest", "ROLE_ADMIN");
		};
	}

	// H2 server bean
	@Profile("dev")
	@Bean(initMethod = "start", destroyMethod = "stop")
	public Server h2Server() throws SQLException {
		return Server.createTcpServer("-tcp","-tcpAllowOthers","-tcpPort","9092");
	}
}
