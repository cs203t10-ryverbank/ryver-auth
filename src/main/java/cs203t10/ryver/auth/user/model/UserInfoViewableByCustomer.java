package cs203t10.ryver.auth.user.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.*;

@Data
public class UserInfoViewableByCustomer implements UserInfo {

    private Long id;

    private String username;

    @JsonProperty("full_name")
    private String fullName;

    private String nric;

    @JsonProperty("phone")
    private String phoneNumber;

    private String address;

}

