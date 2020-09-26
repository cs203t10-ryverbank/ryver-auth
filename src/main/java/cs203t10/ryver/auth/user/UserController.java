package cs203t10.ryver.auth.user;

import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import cs203t10.ryver.auth.security.SecurityUtils;
import cs203t10.ryver.auth.user.model.User;
import cs203t10.ryver.auth.user.model.UserInfo;
import cs203t10.ryver.auth.user.model.UserInfoUpdatableByManager;
import cs203t10.ryver.auth.user.model.UserInfoViewableByCustomer;
import cs203t10.ryver.auth.user.model.UserInfoViewableByManager;
import io.swagger.annotations.ApiOperation;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/customers")
    @RolesAllowed("MANAGER")
    @ApiOperation(value = "Get all user data")
    public List<? extends UserInfo> getCustomers() {
        return userService.findAll().stream().map(user -> {
            UserInfoViewableByManager userInfo = new UserInfoViewableByManager();
            BeanUtils.copyProperties(user, userInfo);
            return userInfo;
        }).collect(Collectors.toList());
    }

    @GetMapping("/customers/{id}")
    @PreAuthorize("principal == #id or hasRole('MANAGER')")
    @ApiOperation(value = "Get a user's data")
    public UserInfo getCustomer(@PathVariable Long id) {
        User user = userService.findById(id);
        // Users without a manager role can only view a subset of customer data.
        UserInfo userInfo;
        if (SecurityUtils.isManagerAuthenticated()) {
            userInfo = new UserInfoViewableByManager();
        } else {
            userInfo = new UserInfoViewableByCustomer();
        }
        BeanUtils.copyProperties(user, userInfo);
        return userInfo;
    }

    @PostMapping("/customers")
    @RolesAllowed("MANAGER")
    @ApiOperation(value = "Add a customer")
    @ResponseStatus(HttpStatus.CREATED)
    public UserInfo addCustomer(@Valid @RequestBody User user){
        User savedUser = userService.saveCustomer(user);
        UserInfoViewableByManager userInfo = new UserInfoViewableByManager();
        BeanUtils.copyProperties(savedUser, userInfo);
        return userInfo;
    }

    @PutMapping("/customers/{id}")
    @RolesAllowed("MANAGER")
    @ApiOperation(value = "Update a user's details",
            notes = "Only fields defined in the request body will be updated.")
    public UserInfo updateCustomer(@PathVariable Long id,
            @Valid @RequestBody UserInfoUpdatableByManager newUserInfo) {
        // Users without a manager role can only view a subset of customer data.
        User updatedUser = userService.updateUser(id, newUserInfo);
        UserInfo infoToView;
        if (SecurityUtils.isManagerAuthenticated()) {
            infoToView = new UserInfoViewableByManager();
        } else {
            infoToView = new UserInfoViewableByCustomer();
        }
        BeanUtils.copyProperties(updatedUser, infoToView);
        return infoToView;
    }

}

