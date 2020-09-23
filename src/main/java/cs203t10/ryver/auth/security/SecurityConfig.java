package cs203t10.ryver.auth.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
    prePostEnabled = true, // Enables @PreAuthorize and @PostAuthorize
    securedEnabled = true, // Enables @Secured
    jsr250Enabled = true   // Enables @RolesAllowed
)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;

    /**
     * Attach the user details and password encoder.
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
            .userDetailsService(userDetailsService)
            .passwordEncoder(encoder());
    }

    /**
     * Allow all requests by default, and control access with method security.
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .httpBasic()
        .and()
            .formLogin().disable()
            .csrf().disable()
            .headers().disable();
    }

    /**
     * Bean annotation is used to declare a PasswordEncoder bean in the Spring application context.
     * Any calls to encoder() will then be intercepted to return the bean instance.
     */
    @Bean
    public BCryptPasswordEncoder encoder() {
        // auto-generate a random salt internally
        return new BCryptPasswordEncoder();
    }
}
