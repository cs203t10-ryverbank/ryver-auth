package cs203.g10.ryver.config;

import org.apache.catalina.core.ApplicationContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import cs203.g10.ryver.user.User;
import cs203.g10.ryver.user.UserRepository;

@Component
@Order(1)
public class DefaultUsers implements CommandLineRunner {

    @Autowired
    private BCryptPasswordEncoder encoder;

    @Autowired
    private UserRepository userRepo;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Running custom default user service.");
    }

    public void addDefaultUser(User user) {
        System.out.println("[Add user]: " + userRepo.save(user).getUsername());
    }
}
        // BCryptPasswordEncoder encoder = ctx.getBean(BCryptPasswordEncoder.class);

        // User defaultManager = User.builder()
        //         .username("johnsmith").password(encoder.encode("goodpassword"))
        //         .authorities("ROLE_MANAGER")
        //         .fullName("John Smith").address("12 Third Street")
        //         .nric("S9876543Z").phoneNumber("+6598765432")
        //         .build();
        // addDefaultUser(ctx, defaultManager);

        // User defaultCustomer = User.builder()
        //         .username("janedoe").password(encoder.encode("badpassword"))
        //         .authorities("ROLE_USER")
        //         .fullName("Jane Doe").address("4 Fifth Avenue")
        //         .nric("S9812345Z").phoneNumber("+6587654321")
        //         .build();
        // addDefaultUser(ctx, defaultUser);
