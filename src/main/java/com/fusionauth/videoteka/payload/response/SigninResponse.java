package com.fusionauth.videoteka.payload.response;


import io.fusionauth.domain.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SigninResponse {

    private String id;

    private String token;

    private User user;

}
