package cs203t10.ryver.auth.user.model;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.*;

@Entity
@Getter @Setter @Builder(toBuilder = true)
@AllArgsConstructor @NoArgsConstructor
@EqualsAndHashCode(callSuper = false) @ToString
public class User implements UserDetails {
    private static final long serialVersionUID = 1L;

    // TODO: Use a unique ID instead.
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    @NotNull(message = "Username cannot be null")
    @Size(min = 5, max = 20, message = "Username should be between 5 and 20 characters")
    private String username;

    @NotNull(message = "Password cannot be null")
    @Size(min = 8, message = "Password must be at least 8 characters")
    private String password;

    @JsonProperty("authorities")
    @NotNull(message = "Authorities cannot be null")
    private String authString;

    @JsonProperty("authorities_array")
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Stream.of(authString.split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @JsonProperty("full_name")
    private String fullName;

    @Pattern(regexp = "^[STFG]\\d{7}[A-JZ]$")
    private String nric;

    @JsonProperty("phone")
    @Pattern(regexp = "^(\\+65)?[689]\\d{7}$")
    private String phoneNumber;

    private String address;

    @Builder.Default
    @Setter(AccessLevel.NONE)
    private boolean accountNonExpired = true;

    @Builder.Default
    @Setter(AccessLevel.NONE)
    private boolean accountNonLocked = true;

    @Builder.Default
    @Setter(AccessLevel.NONE)
    private boolean credentialsNonExpired = true;

    @JsonProperty("active")
    @Builder.Default
    private boolean enabled = true;
}
