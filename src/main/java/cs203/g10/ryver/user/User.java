package cs203.g10.ryver.user;

import java.util.Arrays;
import java.util.Collection;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
@EqualsAndHashCode @ToString
public class User implements UserDetails {
    private static final long serialVersionUID = 1L;

    // TODO: Use a unique ID instead.
    private @Id @GeneratedValue (strategy = GenerationType.IDENTITY) Long id;

    @NotNull(message = "Name cannot be null")
    private String fullName;

    @NotNull(message = "NRIC cannot be null")
    @Pattern(regexp = "^[STFG]\\d{7}[A-JZ]$")
    private String nric;

    @NotNull(message = "Phone number cannot be null")
    @Pattern(regexp = "\\(+65)?[689]\\d{7}")
    private String phoneNumber;

    private String address;

    @NotNull(message = "Username cannot be null")
    @Size(min = 5, max = 20, message = "Username should be between 5 and 20 characters")
    private String username;

    private String password;

    private String authorities;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Arrays.asList(new SimpleGrantedAuthority(authorities));
    }

    private boolean active;

    private boolean accountNonExpired;

    private boolean accountNonLocked;

    private boolean credentialsNonExpired;

    private boolean enabled;
}

