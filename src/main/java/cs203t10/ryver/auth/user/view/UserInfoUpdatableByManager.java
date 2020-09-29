package cs203t10.ryver.auth.user.view;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.*;

@Data
public class UserInfoUpdatableByManager implements UserInfo {

    private String username;

    @JsonProperty("authorities")
    private String authString;

    @JsonProperty("full_name")
    private String fullName;

    private String nric;

    @JsonProperty("phone")
    private String phoneNumber;

    private String address;

    @JsonProperty("active")
    private Boolean enabled;

}

