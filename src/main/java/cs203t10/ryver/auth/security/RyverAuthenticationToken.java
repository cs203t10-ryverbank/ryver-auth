package cs203t10.ryver.auth.security;

import java.util.Collection;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

/**
 * An extension to {@link UsernamePasswordAuthenticationToken} to carry extra
 * information about the user that was logged into.
 */
public class RyverAuthenticationToken extends UsernamePasswordAuthenticationToken {
	private static final long serialVersionUID = 1L;

    private String username;

	public RyverAuthenticationToken(Collection<? extends GrantedAuthority> authorities) {
        this(null, null, authorities);
    }

    public RyverAuthenticationToken(Long id, String username,
            Collection<? extends GrantedAuthority> authorities) {
        super(id, null, authorities);
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

}

