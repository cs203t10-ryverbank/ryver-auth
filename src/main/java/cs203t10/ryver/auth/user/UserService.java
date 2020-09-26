package cs203t10.ryver.auth.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import static cs203t10.ryver.auth.user.UserException.UserAlreadyExistsException;

@Service
public class UserService {

    @Autowired
    private BCryptPasswordEncoder encoder;

    @Autowired
    private UserRepository userRepo;

    public List<User> findAll() {
        return userRepo.findAll();
    }

    /**
     * Save a user as a customer.
     * @return The user as a customer.
     */
    public User saveCustomer(User user) {
        return saveAndHashPassword(user.toBuilder()
                .authorities("ROLE_USER").build());
    }

    /**
     * Save the user to the repository with a hashed password.
     * The original user object is not mutated.
     * @return The user with a hashed password.
     */
    public User saveAndHashPassword(User user) {
        try {
            return userRepo.save(user.toBuilder().password(encoder.encode(user.getPassword())).build());
        } catch (DataIntegrityViolationException e) {
            throw new UserAlreadyExistsException(user.getUsername());
        }
    }
}
