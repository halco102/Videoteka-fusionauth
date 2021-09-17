package com.fusionauth.videoteka.service;

import com.fusionauth.videoteka.payload.request.SigninRequest;
import com.fusionauth.videoteka.payload.request.SignupRequest;
import com.fusionauth.videoteka.payload.response.SigninResponse;
import com.fusionauth.videoteka.payload.response.SignupResponse;
import io.fusionauth.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.SortedSet;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthRepository authRepository;

    public SigninResponse signinResponse (SigninRequest signinRequest) {
        return authRepository.signIn(signinRequest);
    }

    public SignupResponse signupResponse (SignupRequest signupRequest) {
        return authRepository.signUp(signupRequest);
    }

    public User getUserByJwt (String jwt) {
        return authRepository.getUserInfoByJWT(jwt);
    }

    public User getUserByUsername (String username) {
        return authRepository.getUserByUsername(username);
    }

    public SortedSet<String> getUserRolesFromApp(String appId, String userId) {
        return authRepository.getUserRolesFromApp(appId, userId);
    }

    public boolean validateJwt(String jwt){
        return authRepository.validateJwt(jwt);
    }

    public SignupResponse createAdmin(SignupRequest request) {
        return authRepository.createAdmin(request);
    }
}
