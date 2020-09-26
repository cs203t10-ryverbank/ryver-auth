package cs203t10.ryver.auth.user;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public class UserException {

    @ResponseStatus(value = HttpStatus.CONFLICT, reason = "Username %s already exists")
    public static class UserAlreadyExistsException extends RuntimeException {
		private static final long serialVersionUID = 1L;

        public UserAlreadyExistsException(String username) {
            super(String.format("User %s already exists", username));
        }
    }

}

