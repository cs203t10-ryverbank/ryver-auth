package cs203t10.ryver.auth.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.stream.Collectors;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.auth0.jwt.JWT;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import cs203t10.ryver.auth.user.User;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;
import static cs203t10.ryver.auth.security.SecurityConstants.AUTHORITIES_KEY;
import static cs203t10.ryver.auth.security.SecurityConstants.BASIC_PREFIX;
import static cs203t10.ryver.auth.security.SecurityConstants.BEARER_PREFIX;
import static cs203t10.ryver.auth.security.SecurityConstants.EXPIRATION_TIME;
import static cs203t10.ryver.auth.security.SecurityConstants.AUTH_HEADER_KEY;
import static cs203t10.ryver.auth.security.SecurityConstants.SECRET;
import static cs203t10.ryver.auth.security.SecurityConstants.UID_KEY;

/**
 * Handle all authentication attempts on the `/login` route.
 *
 * {@link UsernamePasswordAuthenticationFilter} registers itself on the
 * `/login` route. Any request made to the `/login` endpoint will activate
 * this authentication filter.
 */
public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private AuthenticationManager authManager;

    public JWTAuthenticationFilter(AuthenticationManager authManager) {
        this.authManager = authManager;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
            HttpServletResponse response) throws AuthenticationException {
        String[] usernamePassword = getUsernamePasswordFromBasicAuth(request);

        return authManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        usernamePassword[0],
                        usernamePassword[1],
                        new ArrayList<>()));
    }

    private String[] getUsernamePasswordFromBasicAuth(HttpServletRequest request) {
        // In the form of: "Basic <encoded username and password>"
        final String authHeader = request.getHeader(AUTH_HEADER_KEY);
        if (authHeader == null || !authHeader.startsWith(BASIC_PREFIX)) {
            throw new RuntimeException("Invalid Basic Authorization header");
        }
        final String encodedCred = authHeader.replace(BASIC_PREFIX, "");
        final String decodedCred = new String(Base64.getDecoder().decode(encodedCred));
        // In the form of "<username>:<password>"
        return decodedCred.split(":");
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
            HttpServletResponse response, FilterChain chain,
            Authentication authResult) throws IOException, ServletException {

        User user = (User) authResult.getPrincipal();

        // Serialize all granted authorities into a comma-separated string.
        final String authorities = authResult.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.joining(","));

        String token = JWT.create()
            .withSubject(user.getUsername())
            .withClaim(UID_KEY, user.getId())
            .withClaim(AUTHORITIES_KEY, authorities)
            .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
            .sign(HMAC512(SECRET.getBytes()));

        response.addHeader(AUTH_HEADER_KEY, BEARER_PREFIX + token);
    }

}
