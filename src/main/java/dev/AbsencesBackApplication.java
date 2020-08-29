package dev;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.PropertySource;

import dev.security.JwtConfig;

@SpringBootApplication
@PropertySource("classpath:application.properties")
@EnableConfigurationProperties(JwtConfig.class)
public class AbsencesBackApplication {

	public static void main(String[] args) {
		SpringApplication.run(AbsencesBackApplication.class, args);
	}

}
