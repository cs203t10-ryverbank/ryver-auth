package cs203t10.ryver.auth.user;

import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import cs203t10.ryver.auth.user.UserException.UserNotFoundException;

@RestController
@ControllerAdvice
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/customers")
    @RolesAllowed("MANAGER")
    public List<User> getCustomers() {
        return userService.findAll();
    }

    /**
     * Using BCrypt encoder to encrypt the password for storage
     * @param user
     * @return
     */
    @PostMapping("/customers")
    @RolesAllowed("MANAGER")
    public User addCustomer(@Valid @RequestBody User user){
        return userService.saveCustomer(user);
    }

    @PutMapping("/customers/{id}")
    @RolesAllowed("MANAGER")
    public User updateCustomer(@PathVariable Long id,
            @Valid @RequestBody UserUpdatableInfo userInfo) {
        System.out.println(userInfo);
        User updatedUser = userService.updateUser(id, userInfo);
        if (updatedUser == null) {
            throw new UserNotFoundException(id);
        }
        return updatedUser;
    }

}

