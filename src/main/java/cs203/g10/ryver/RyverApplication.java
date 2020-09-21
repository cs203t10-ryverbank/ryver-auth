package cs203.g10.ryver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import cs203.g10.ryver.user.User;
import cs203.g10.ryver.user.UserRepository;

@SpringBootApplication
public class RyverApplication {
	public static void main(String[] args) {
		ApplicationContext ctx = SpringApplication.run(RyverApplication.class, args);

        UserRepository userRepo = ctx.getBean(UserRepository.class);
        BCryptPasswordEncoder encoder = ctx.getBean(BCryptPasswordEncoder.class);
        System.out.println("[Add user]: " + userRepo.save(
            new User("admin", encoder.encode("goodpassword"), "ROLE_ADMIN")).getUsername());
	}
}
