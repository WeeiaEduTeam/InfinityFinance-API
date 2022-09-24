package com.github.WeeiaEduTeam.InfinityFinanceAPI;

import com.github.WeeiaEduTeam.InfinityFinanceAPI.role.RoleType;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.stream.Collectors;

@Configuration
@SpringBootApplication
public class InfinityFinanceApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(InfinityFinanceApiApplication.class, args);
	}

}
