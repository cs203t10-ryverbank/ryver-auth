package cs203.g10.ryver.user;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Pattern;

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
public class User {
    enum Role {
        USER,
        MANAGER,
        ANALYST,
    }

    // TODO: Use a unique ID instead.
    private @Id @GeneratedValue (strategy = GenerationType.IDENTITY) Long id;

    private String fullName;

    @Pattern(regexp = "^[STFG]\\d{7}[A-JZ]$")
    private String nric;

    @Pattern(regexp = "\\(+65)?[689]\\d{7}")
    private String phoneNumber;

    private String address;

    private String username;

    private String password;

    private Role authorities;

    private boolean active;
}
