package cs203t10.ryver.auth.user.view;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.*;

@Data
public class UserNewPassword implements UserInfo {

    @NotNull(message = "Password cannot be null")
    @Size(min = 8, message = "Password must be at least 8 characters")
    private String password;

}

