package cs203t10.ryver.auth.user;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.*;

@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
@EqualsAndHashCode @ToString
public class UserUpdatableInfo {

    @JsonProperty("full_name")
    String fullName;

    String nric;

    @JsonProperty("phone")
    String phoneNumber;

    String address;

}
