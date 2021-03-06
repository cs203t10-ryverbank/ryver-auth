package cs203t10.ryver.auth.user;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void getCustomersUnauthorized() throws Exception {
        mockMvc.perform(get("/customers")).andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = { "USER" })
    public void getCustomersForbidden() throws Exception {
        mockMvc.perform(get("/customers")).andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = { "MANAGER" })
    public void getCustomers() throws Exception {
        mockMvc.perform(get("/customers")).andExpect(status().isOk());
    }
}
