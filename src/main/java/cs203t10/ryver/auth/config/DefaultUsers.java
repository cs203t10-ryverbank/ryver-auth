package cs203t10.ryver.auth.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import cs203t10.ryver.auth.user.model.User;
import cs203t10.ryver.auth.user.UserService;

import static cs203t10.ryver.auth.user.UserException.UserAlreadyExistsException;

@Component
@Order(1)
public class DefaultUsers implements CommandLineRunner {

    @Autowired
    private UserService userService;

    @Override
    public void run(String... args) throws Exception {
        addDefaultUser(User.builder().username("manager_1").password("01_manager_01").authorities("ROLE_MANAGER").build());
        addDefaultUser(User.builder().username("analyst_1").password("01_analyst_01").authorities("ROLE_ANALYST").build());
        addDefaultUser(User.builder().username("analyst_2").password("02_analyst_02").authorities("ROLE_ANALYST").build());
        // Added during tests.
        // addDefaultUser(User.builder().username("good_user_1").password("01_user_01").authorities("ROLE_USER").build());
        // addDefaultUser(User.builder().username("good_user_2").password("02_user_02").authorities("ROLE_USER").build());
    }

    public void addDefaultUser(User user) {
        try {
            System.out.println("[Add user]: " + userService.saveAndHashPassword(user).getUsername());
        } catch (UserAlreadyExistsException e) {
            System.out.println("[Already exists]: " + user.getUsername());
        }
    }
}

