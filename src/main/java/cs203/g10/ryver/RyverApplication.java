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
        User defaultManager = User.builder()
                .username("johnsmith")
                .password(encoder.encode("goodpassword"))
                .authorities("ROLE_MANAGER")
                .fullName("John Smith")
                .nric("S9876543Z")
                .phoneNumber("+6598765432")
                .address("12 Third Street")
                .build();
        User defaultUser = User.builder()
                .username("janedoe")
                .password(encoder.encode("badpassword"))
                .authorities("ROLE_USER")
                .fullName("Jane Doe")
                .nric("S9812345Z")
                .phoneNumber("+6587654321")
                .address("4 Fifth Avenue")
                .build();
        System.out.println("[Add user]: " + userRepo.save(defaultManager).getUsername());
        System.out.println("[Add user]: " + userRepo.save(defaultUser).getUsername());
	}
}
