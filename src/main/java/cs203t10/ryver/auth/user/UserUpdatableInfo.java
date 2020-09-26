package cs203t10.ryver.auth.user;

import lombok.*;

@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
@EqualsAndHashCode @ToString
public class UserUpdatableInfo {

    String fullName;

    String nric;

    String phoneNumber;

    String address;

}
