package cs203t10.ryver.auth.user.view;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.*;

@Data
public class UserInfoViewableByCustomer implements UserInfo {

    private Long id;

    private String username;

    private String fullName;

    private String nric;

    @JsonProperty("phone")
    private String phoneNumber;

    private String address;

}

