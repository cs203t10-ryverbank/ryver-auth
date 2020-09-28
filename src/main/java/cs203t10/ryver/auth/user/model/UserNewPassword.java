package cs203t10.ryver.auth.user.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.*;

@Data
public class UserNewPassword {

    @NotNull(message = "Password cannot be null")
    @Size(min = 8, message = "Password must be at least 8 characters")
    private String password;

}

