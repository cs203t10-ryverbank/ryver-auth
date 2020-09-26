package cs203t10.ryver.auth.user;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public class UserException {

    @ResponseStatus(value = HttpStatus.CONFLICT, reason = "Username already exists")
    public static class UserAlreadyExistsException extends RuntimeException {
		private static final long serialVersionUID = 1L;

        public UserAlreadyExistsException(String username) {
            super(String.format("User %s already exists", username));
        }
    }

    @ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "User not found.")
    public static class UserNotFoundException extends RuntimeException {
        private static final long serialVersionUID = 1L;

        public UserNotFoundException(Long id) {
            super(String.format("User with id: %s not found", id));
        }

        public UserNotFoundException(String username) {
            super(String.format("User with username: %s not found", username));
        }
    }

}

