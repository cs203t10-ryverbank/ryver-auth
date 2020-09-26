package cs203t10.ryver.auth.user;

import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import cs203t10.ryver.auth.user.UserException.UserAlreadyExistsException;

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

}

