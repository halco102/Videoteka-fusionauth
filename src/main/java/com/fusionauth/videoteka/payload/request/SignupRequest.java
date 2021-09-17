package com.fusionauth.videoteka.payload.request;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignupRequest {

    private String appId;

    private String email;

    private String password;

    private String username;

}
