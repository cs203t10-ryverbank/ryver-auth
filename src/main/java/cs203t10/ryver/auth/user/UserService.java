package cs203t10.ryver.auth.user;

import java.beans.FeatureDescriptor;
import java.util.List;
import java.util.stream.Stream;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import cs203t10.ryver.auth.user.UserException.UserNotFoundException;
import cs203t10.ryver.auth.user.model.User;
import cs203t10.ryver.auth.user.model.UserUpdatableInfo;

import static cs203t10.ryver.auth.user.UserException.UserAlreadyExistsException;

@Service
public class UserService {

    @Autowired
    private BCryptPasswordEncoder encoder;

    @Autowired
    private UserRepository userRepo;

    public User findById(long id) {
        return userRepo.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

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

    public User updateUser(long id, UserUpdatableInfo updatedUser) {
        return userRepo.findById(id).map(user -> {
            // Copy over non-null values only.
            BeanUtils.copyProperties(updatedUser, user, getNullPropertyNames(updatedUser));
            return userRepo.save(user);
        }).orElseThrow(() -> new UserNotFoundException(id));
    }

    private String[] getNullPropertyNames(Object source) {
        final BeanWrapper wrappedSource = new BeanWrapperImpl(source);
        return Stream.of(wrappedSource.getPropertyDescriptors())
                .map(FeatureDescriptor::getName)
                .filter(propertyName ->
                        wrappedSource.getPropertyValue(propertyName) == null)
                .toArray(String[]::new);
    }

    public User setActiveOfUser(long id, boolean active) {
        return userRepo.findById(id).map(user -> {
            user.setEnabled(active);
            return userRepo.save(user);
        }).orElseThrow(() -> new UserNotFoundException(id));
    }

}

