package cs203.g10.ryver.user;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class UserControllerTest {
    private static String host = "http://localhost";

    @LocalServerPort
    private int port;

    private String getHostPort() {
        return host + ":" + port + "/";
    }

    @Autowired
    private UserController controller;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void contextLoads() {
        assertThat(controller).isNotNull();
    }

    @Test
    public void getUsersUnauthorized() {
        ResponseEntity<String> response = restTemplate.getForEntity(getHostPort() + "users", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    @WithMockUser(username = "johnsmith", roles = { "MANAGER" })
    public void getUsers() {
        ResponseEntity<String> response = restTemplate.getForEntity(getHostPort() + "users", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        System.out.println(response.getBody());
    }
}
