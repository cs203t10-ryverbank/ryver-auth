package cs203t10.ryver.auth.user;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import cs203t10.ryver.auth.user.UserException.UserNotFoundException;
import cs203t10.ryver.auth.user.view.UserInfo;
import cs203t10.ryver.auth.util.CustomBeanUtils;

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
        return saveNewUser(user.toBuilder()
                .authString("ROLE_USER")
                .build());
    }

    /**
     * Save a new user to the repository.
     */
    public User saveNewUser(User user) {
        try {
            // If a new password is set, encode it.
            String newPassword = user.getPassword();
            if (newPassword != null) {
                System.out.println(newPassword);
                user.setPassword(encoder.encode(newPassword));
            }
            return userRepo.save(user);
        } catch (DataIntegrityViolationException e) {
            throw new UserAlreadyExistsException(user.getUsername());
        }
    }

    /**
     * Update the details of a user in the repository and replace all information.
     */
    public User updateUser(long id, UserInfo newUserInfo) {
        return updateUser(id, newUserInfo, false);
    }

    /**
     * Update the details of a user in the repository.
     * @param shouldMerge If true, null properties of the new info object will
     * represent leaving the current values in place. Otherwise, the user
     * details are fully replaced.
     */
    public User updateUser(long id, UserInfo newUserInfo, boolean shouldMerge) {
        return userRepo.findById(id).map(user -> {
            if (shouldMerge) {
                CustomBeanUtils.copyNonNullProperties(newUserInfo, user);
            } else {
                BeanUtils.copyProperties(newUserInfo, user);
            }
            // If a new password is set, encode it.
            String newPassword = CustomBeanUtils.getPropertyValueWithName(
                    newUserInfo, "password", String.class);
            if (newPassword != null) {
                System.out.println(newPassword);
                user.setPassword(encoder.encode(newPassword));
            }
            return userRepo.save(user);
        }).orElseThrow(() -> new UserNotFoundException(id));
    }

}

