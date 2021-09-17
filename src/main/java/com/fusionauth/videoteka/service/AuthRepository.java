package com.fusionauth.videoteka.service;

import com.fusionauth.videoteka.payload.request.SigninRequest;
import com.fusionauth.videoteka.payload.request.SignupRequest;
import com.fusionauth.videoteka.payload.response.SigninResponse;
import com.fusionauth.videoteka.payload.response.SignupResponse;
import io.fusionauth.domain.User;
import org.springframework.stereotype.Repository;

import java.util.SortedSet;

@Repository
public interface AuthRepository {

    SigninResponse signIn (SigninRequest request);

    SignupResponse signUp (SignupRequest request);

    void deleteUserById(String userId);

    User getUserInfoByJWT(String jwt);

    User getUserByUsername(String username);

    SortedSet<String> getUserRolesFromApp(String appId, String userId);

    boolean validateJwt(String jwt);

    SignupResponse createAdmin(SignupRequest signupRequest);
}
