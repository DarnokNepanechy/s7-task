package ru.dragulaxis.s7task;

import org.h2.tools.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

import java.sql.SQLException;

@SpringBootApplication
public class S7TaskApplication {

	public static void main(String[] args) {
		SpringApplication.run(S7TaskApplication.class, args);
	}

	@Profile("dev")
	@Bean(initMethod = "start", destroyMethod = "stop")
	public Server h2Server() throws SQLException {
		return Server.createTcpServer("-tcp","-tcpAllowOthers","-tcpPort","9092");
	}

}
