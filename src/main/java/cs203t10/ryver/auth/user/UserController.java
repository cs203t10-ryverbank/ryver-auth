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
import cs203t10.ryver.auth.util.CustomBeanUtils;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import static cs203t10.ryver.auth.user.UserException.UserUpdateForbiddenException;

@RestController
public class UserController {

    @Autowired
    private UserService userService;


    @GetMapping("/customers")
    @RolesAllowed("MANAGER")
    @Operation(summary = "Get all user data")
    @ApiResponse(responseCode = "200", 
                content = @Content(mediaType = "application/json", 
                schema = @Schema(implementation = UserInfoViewableByManager[].class)))
    public List<? extends UserInfo> getCustomers() {
        return userService.findAll().stream().map(user -> {
            UserInfoViewableByManager userInfo = new UserInfoViewableByManager();
            BeanUtils.copyProperties(user, userInfo);
            return userInfo;
        }).collect(Collectors.toList());
    }


    @GetMapping("/customers/{id}")
    @PreAuthorize("principal.uid == #id or hasRole('MANAGER')")
    @Operation(summary = "Get a user's data")
    @ApiResponse(responseCode = "200", 
                content = @Content(mediaType = "application/json", 
                schema = @Schema(implementation = UserInfo.class)))
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
    @Operation(summary = "Add a customer")
    @ApiResponse(responseCode = "201", 
                content = @Content(mediaType = "application/json", 
                schema = @Schema(implementation = UserInfo.class)))
    @ResponseStatus(HttpStatus.CREATED)
    public UserInfo addCustomer(@Valid @RequestBody User user){
        User savedUser = userService.saveCustomer(user);
        UserInfoViewableByManager viewableInfo = new UserInfoViewableByManager();
        BeanUtils.copyProperties(savedUser, viewableInfo);
        return viewableInfo;
    }


    @PutMapping("/customers/{id}")
    @PreAuthorize("principal.uid == #id or hasRole('MANAGER')")
    @Operation(summary = "Update a user's details",
            description = "All of the user's updatable details will be replaced by the request body.")
    @ApiResponse(responseCode = "200", 
            content = @Content(mediaType = "application/json", 
            schema = @Schema(implementation = UserInfo.class)))
    public UserInfo updateCustomer(@PathVariable Long id,
            @Valid @RequestBody UserInfoUpdatableByManager newUserInfo) {

        boolean isManager = SecurityUtils.isManagerAuthenticated();
        // Users without a manager role can only update a subset of customer data.
        UserInfo updatableInfo = isManager
                ? new UserInfoUpdatableByManager()
                : new UserInfoUpdatableByCustomer();

        // Check if the properties updated are permitted.

        CustomBeanUtils.copyNonNullProperties( newUserInfo, updatableInfo);
        User updatedUser = userService.updateUser(id, updatableInfo, true);

        // Transfer entity properties to data transfer object.
        UserInfo viewableInfo = isManager
                ? new UserInfoViewableByManager()
                : new UserInfoViewableByCustomer();
        BeanUtils.copyProperties(updatedUser, viewableInfo);

        return viewableInfo;
    }


    @PatchMapping("/customers/{id}")
    @PreAuthorize("principal.uid == #id or hasRole('MANAGER')")
    @Operation(summary = "Patch a user's details",
            description = "Only fields defined in the request body will be updated. "
            + "Null fields signify that the property should be left as-is.")
    @ApiResponse(responseCode = "200", 
                content = @Content(mediaType = "application/json", 
                schema = @Schema(implementation = UserInfo.class)))
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

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/reset")
    @RolesAllowed("MANAGER")
    @Hidden
    public void resetCustomers() {
        userService.resetCustomers();
    }

}

