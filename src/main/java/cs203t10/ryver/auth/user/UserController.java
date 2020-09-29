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
import cs203t10.ryver.auth.user.view.UserInfo;
import cs203t10.ryver.auth.user.view.UserInfoUpdatableByCustomer;
import cs203t10.ryver.auth.user.view.UserInfoUpdatableByManager;
import cs203t10.ryver.auth.user.view.UserInfoViewableByCustomer;
import cs203t10.ryver.auth.user.view.UserInfoViewableByManager;
import cs203t10.ryver.auth.user.view.UserNewPassword;
import cs203t10.ryver.auth.util.CustomBeanUtils;
import io.swagger.annotations.ApiOperation;

import static cs203t10.ryver.auth.user.UserException.UserUpdateForbiddenException;

@RestController
public class UserController {

    @Autowired
    private UserService userService;


    @GetMapping("/customers")
    @RolesAllowed("MANAGER")
    @ApiOperation(value = "Get all user data",
            response = UserInfoViewableByManager[].class)
    public List<? extends UserInfo> getCustomers() {
        return userService.findAll().stream().map(user -> {
            UserInfoViewableByManager userInfo = new UserInfoViewableByManager();
            BeanUtils.copyProperties(user, userInfo);
            return userInfo;
        }).collect(Collectors.toList());
    }


    @GetMapping("/customers/{id}")
    @PreAuthorize("principal.id == #id or hasRole('MANAGER')")
    @ApiOperation(value = "Get a user's data",
            response = UserInfoViewableByManager.class)
    public UserInfo getCustomer(@PathVariable Long id) {
        User user = userService.findById(id);
        // Users without a manager role can only view a subset of customer data.
        UserInfo viewableInfo = SecurityUtils.isManagerAuthenticated()
                ? new UserInfoViewableByManager()
                : new UserInfoViewableByCustomer();
        BeanUtils.copyProperties(user, viewableInfo);
        return viewableInfo;
    }


    @PostMapping("/customers")
    @RolesAllowed("MANAGER")
    @ApiOperation(value = "Add a customer",
            response = UserInfoViewableByManager.class)
    @ResponseStatus(HttpStatus.CREATED)
    public UserInfo addCustomer(@Valid @RequestBody User user){
        User savedUser = userService.saveCustomer(user);
        UserInfoViewableByManager viewableInfo = new UserInfoViewableByManager();
        BeanUtils.copyProperties(savedUser, viewableInfo);
        return viewableInfo;
    }


    @PostMapping("/customers/{id}/update_password")
    @PreAuthorize("principal.id == #id or hasRole('MANAGER')")
    @ApiOperation(value = "Update a user's password",
            notes = "Password will be hashed by the API.",
            response = UserInfoViewableByManager.class)
    public UserInfo updateCustomerPassword(@PathVariable Long id,
            @Valid @RequestBody UserNewPassword newPassword) {
        User updatedUser = userService.updateUserPassword(id, newPassword.getPassword());

        // Transfer entity properties to data transfer object.
        UserInfo viewableInfo = SecurityUtils.isManagerAuthenticated()
                ? new UserInfoViewableByManager()
                : new UserInfoViewableByCustomer();
        BeanUtils.copyProperties(updatedUser, viewableInfo);

        return viewableInfo;
    }


    @PutMapping("/customers/{id}")
    @PreAuthorize("principal.id == #id or hasRole('MANAGER')")
    @ApiOperation(value = "Update a user's details",
            notes = "All of the user's updatable details will be replaced by the request body.",
            response = UserInfoViewableByManager.class)
    public UserInfo updateCustomer(@PathVariable Long id,
            @Valid @RequestBody UserInfoUpdatableByManager newUserInfo) {

        boolean isManager = SecurityUtils.isManagerAuthenticated();
        // Users without a manager role can only update a subset of customer data.
        UserInfo updatableInfo = isManager
                ? new UserInfoUpdatableByManager()
                : new UserInfoUpdatableByCustomer();

        // Check if the properties updated are permitted.
        if (!CustomBeanUtils.nonNullIsSubsetOf(newUserInfo, updatableInfo)) {
            throw new UserUpdateForbiddenException();
        }

        User updatedUser = userService.updateUser(id, newUserInfo);

        // Transfer entity properties to data transfer object.
        UserInfo viewableInfo = isManager
                ? new UserInfoViewableByManager()
                : new UserInfoViewableByCustomer();
        BeanUtils.copyProperties(updatedUser, viewableInfo);

        return viewableInfo;
    }


    @PatchMapping("/customers/{id}")
    @PreAuthorize("principal.id == #id or hasRole('MANAGER')")
    @ApiOperation(value = "Patch a user's details",
            notes = "Only fields defined in the request body will be updated."
            + "Null fields signify that the property should be left as-is.",
            response = UserInfoViewableByManager.class)
    public UserInfo patchCustomer(@PathVariable Long id,
            @Valid @RequestBody UserInfoUpdatableByManager newUserInfo) {

        boolean isManager = SecurityUtils.isManagerAuthenticated();
        // Users without a manager role can only update a subset of customer data.
        UserInfo updatableInfo = isManager
                ? new UserInfoUpdatableByManager()
                : new UserInfoUpdatableByCustomer();

        // Check if the properties updated are permitted.
        if (!CustomBeanUtils.nonNullIsSubsetOf(newUserInfo, updatableInfo)) {
            throw new UserUpdateForbiddenException();
        }

        User updatedUser = userService.updateUser(id, newUserInfo, true);

        // Transfer entity properties to data transfer object.
        UserInfo viewableInfo = isManager
                ? new UserInfoViewableByManager()
                : new UserInfoViewableByCustomer();
        BeanUtils.copyProperties(updatedUser, viewableInfo);

        return viewableInfo;
    }

}

