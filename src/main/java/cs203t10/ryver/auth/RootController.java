package cs203t10.ryver.auth;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RootController {

    @GetMapping("/")
    public String getRoot() {
        return "Welcome to the Ryver Auth Service";
    }

}

