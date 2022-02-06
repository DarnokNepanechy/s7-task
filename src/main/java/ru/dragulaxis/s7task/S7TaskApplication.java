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

	// Этот метод запускается после инициализации приложения - тут первоночальные данные
	@Bean
	CommandLineRunner run(UserService userService) {
		return  args -> {
			userService.saveRole(new Role(null, "ROLE_ADMIN"));
			userService.saveRole(new Role(null, "ROLE_USER"));

			userService.saveUser(new User(null, "JohnTravolta", "1234", new HashSet<>(), new HashSet<>()));
			userService.saveUser(new User(null, "WillSmith", "1234", new HashSet<>(), new HashSet<>()));
			userService.saveUser(new User(null, "JimCarry", "1234", new HashSet<>(), new HashSet<>()));
			userService.saveUser(new User(null, "ArnoldSchwarzenegger", "arnold", new HashSet<>(), new HashSet<>()));

			userService.addRoleToUser("JohnTravolta", "ROLE_ADMIN");
			userService.addRoleToUser("JohnTravolta", "ROLE_USER");
			userService.addRoleToUser("WillSmith", "ROLE_USER");
			userService.addRoleToUser("JimCarry", "ROLE_USER");
			userService.addRoleToUser("ArnoldSchwarzenegger", "ROLE_USER");
		};
	}

	// H2 server bean
	@Profile("dev")
	@Bean(initMethod = "start", destroyMethod = "stop")
	public Server h2Server() throws SQLException {
		return Server.createTcpServer("-tcp","-tcpAllowOthers","-tcpPort","9092");
	}
}
