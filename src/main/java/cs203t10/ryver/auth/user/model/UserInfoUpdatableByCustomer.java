package cs203t10.ryver.auth.user.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.*;

@Data
public class UserInfoUpdatableByCustomer implements UserInfo {

    private String password;

    @JsonProperty("phone")
    private String phoneNumber;

    private String address;

}

