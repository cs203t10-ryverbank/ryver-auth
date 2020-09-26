package cs203t10.ryver.auth.user.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.*;

@Data
public class UserInfoUpdatableByManager implements UserInfo {

    private String username;

    private String password;

    private String authString;

    @JsonProperty("full_name")
    private String fullName;

    private String nric;

    @JsonProperty("phone")
    private String phoneNumber;

    private String address;

    @JsonProperty("active")
    private boolean enabled;

}
