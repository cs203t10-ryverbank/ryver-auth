package cs203.g10.ryver.user;

import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private BCryptPasswordEncoder encoder;

    @GetMapping("/users")
    @RolesAllowed("MANAGER")
    public List<User> getUsers() {
        return userRepo.findAll();
    }

    /**
     * Using BCrypt encoder to encrypt the password for storage
     * @param user
     * @return
     */
    @PostMapping("/users")
    @RolesAllowed("MANAGER")
    public User addUser(@Valid @RequestBody User user){
        user.setPassword(encoder.encode(user.getPassword()));
        return userRepo.save(user);
    }
}

