package cs203.g10.ryver.user;

import java.util.List;

import javax.validation.Valid;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {
    private UserRepository userRepo;
    private BCryptPasswordEncoder encoder;

    public UserController(UserRepository userRepo, BCryptPasswordEncoder encoder){
        this.userRepo = userRepo;
        this.encoder = encoder;
    }

    @GetMapping("/users")
    public List<User> getUsers() {
        return userRepo.findAll();
    }

    /**
     * Using BCrypt encoder to encrypt the password for storage
     * @param user
     * @return
     */
    @PostMapping("/users")
    public User addUser(@Valid @RequestBody User user){
        user.setPassword(encoder.encode(user.getPassword()));
        return userRepo.save(user);
    }
}

