package jonatas.AutenticacoJWT;

import jonatas.AutenticacoJWT.Models.User;
import jonatas.AutenticacoJWT.Repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class AutenticacoJwtApplication {

	public static void main(String[] args) {
		SpringApplication.run(AutenticacoJwtApplication.class, args);
	}

	@Bean
	public CommandLineRunner demoData(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		return args -> {
			userRepository.findByUsername("admin").ifPresentOrElse(
					user -> { /* Já existe */ },
					() -> {
						User admin = new User(null, "admin", passwordEncoder.encode("123456"), "ADMIN");
						userRepository.save(admin);
					}
			);
			userRepository.findByUsername("user").ifPresentOrElse(
					user -> { /* Já existe */ },
					() -> {
						User regularUser = new User(null, "user", passwordEncoder.encode("password"), "USER");
						userRepository.save(regularUser);
					}
			);
		};
	}

}
