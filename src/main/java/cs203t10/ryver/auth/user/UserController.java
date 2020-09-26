package cs203t10.ryver.auth.user;

import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import cs203t10.ryver.auth.user.UserException.UserNotFoundException;
import io.swagger.annotations.ApiOperation;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/customers")
    @RolesAllowed("MANAGER")
    @ApiOperation(value = "Get all user data")
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
    @ApiOperation(value = "Add a customer")
    @ResponseStatus(HttpStatus.CREATED)
    public User addCustomer(@Valid @RequestBody User user){
        return userService.saveCustomer(user);
    }

    @PutMapping("/customers/{id}")
    @RolesAllowed("MANAGER")
    @ApiOperation(value = "Update a customer's details",
            notes = "Only fields defined in the request body will be updated.")
    public User updateCustomer(@PathVariable Long id,
            @Valid @RequestBody UserUpdatableInfo userInfo) {
        return userService.updateUser(id, userInfo);
    }

    @PutMapping("/customers/{id}/disable")
    @RolesAllowed("MANAGER")
    @ApiOperation(value = "Disable a customer's account")
    public User disableCustomer(@PathVariable Long id) {
        return userService.setActiveOfUser(id, false);
    }

    @PutMapping("/customers/{id}/enable")
    @RolesAllowed("MANAGER")
    @ApiOperation(value = "Enable a customer's account")
    public User enableCustomer(@PathVariable Long id) {
        return userService.setActiveOfUser(id, true);
    }

}

