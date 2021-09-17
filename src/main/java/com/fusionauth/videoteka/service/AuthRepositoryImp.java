package com.fusionauth.videoteka.service;

import com.fusionauth.videoteka.exception.*;
import com.fusionauth.videoteka.payload.request.SigninRequest;
import com.fusionauth.videoteka.payload.request.SignupRequest;
import com.fusionauth.videoteka.payload.response.SigninResponse;
import com.fusionauth.videoteka.payload.response.SignupResponse;
import com.inversoft.error.Errors;
import com.inversoft.rest.ClientResponse;
import io.fusionauth.client.FusionAuthClient;
import io.fusionauth.domain.User;
import io.fusionauth.domain.UserRegistration;
import io.fusionauth.domain.api.LoginRequest;
import io.fusionauth.domain.api.LoginResponse;
import io.fusionauth.domain.api.UserResponse;
import io.fusionauth.domain.api.jwt.ValidateResponse;
import io.fusionauth.domain.api.user.RegistrationRequest;
import io.fusionauth.domain.api.user.RegistrationResponse;
import io.fusionauth.jwt.domain.JWT;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
@Slf4j
public class AuthRepositoryImp implements AuthRepository{

    private final FusionAuthClient fusionAuthClient;

    @Override
    public SignupResponse createAdmin(SignupRequest request) {
        String email = request.getEmail().toLowerCase().trim();
        List<String> roles = new ArrayList<>();
        roles.add("ROLE_USER");
        roles.add("ROLE_ADMIN");

        if (validateEmail(email)) {


            ClientResponse<RegistrationResponse, Errors> response = fusionAuthClient.register(
                    null, new RegistrationRequest(
                            new User()
                                    .with(user -> user.email = email)
                                    .with(user -> user.username = request.getUsername())
                                    .with(user -> user.password = request.getPassword()),
                            new UserRegistration().with(reg -> reg.applicationId = UUID.fromString(request.getAppId()))
                                    .with(reg -> {
                                        for (String r: roles
                                             ) {
                                            reg.roles.add(r);
                                        }
                                    }),
                            false,
                            false

                    )
            );

            // provjeraje response
            if (response.wasSuccessful()) {
                log.info("User se uspjesno prijavio na appId : " + request.getAppId());
                SignupResponse signupResponse = new SignupResponse();
                signupResponse.setEmail(response.successResponse.user.email);
                signupResponse.setUsername(response.successResponse.user.username);
                signupResponse.setId(String.valueOf(response.successResponse.user.id));

                return signupResponse;
            }

            throw  resolveException(response);

        }

        throw new BadRequestException("Email is not valid");

    }

    @Override
    public boolean validateJwt(String jwt) {
        ClientResponse<ValidateResponse, Void> response = fusionAuthClient.validateJWT(jwt);

        if (response.wasSuccessful()) {
            return true;
        }

        return false;
    }

    @Override
    public SigninResponse signIn(SigninRequest request) {

        String email = request.getEmail();

        if(validateEmail(email)) {

            ClientResponse<LoginResponse, Errors> response = fusionAuthClient.login(new LoginRequest(UUID.fromString(request.getAppId()), request.getEmail(), request.getPassword()));

            if(response.wasSuccessful()) {

                return new SigninResponse(String.valueOf(response.successResponse.user.id), response.successResponse.token, response.successResponse.user);
            }
            throw resolveException(response);
        }
        throw new BadRequestException("Email not valid");
    }

    @Override
    public SignupResponse signUp(SignupRequest request) {
        String email = request.getEmail().toLowerCase().trim();
        String defaultRole = "ROLE_USER";

        if (validateEmail(email)) {

            /*
            * Fusionauth api call za registrovanje novih usera na osnovu applikacije i dodjeljivanja rola na toj odredjenoj applikaciji,
            * user preko req proslijedjuje appId, username, email, password
            * svaki kreirani user ce imati role user
            * */

            ClientResponse<RegistrationResponse, Errors> response = fusionAuthClient.register(
                    null, new RegistrationRequest(
                            new User()
                                    .with(user -> user.email = email)
                                    .with(user -> user.username = request.getUsername())
                                    .with(user -> user.password = request.getPassword()),
                            new UserRegistration().with(reg -> reg.applicationId = UUID.fromString(request.getAppId()))
                                    .with(reg -> reg.roles.add(defaultRole)),
                            false,
                            false

                    )
            );

            // provjeraje response
            if (response.wasSuccessful()) {
                log.info("User se uspjesno prijavio na appId : " + request.getAppId());
                SignupResponse signupResponse = new SignupResponse();
                signupResponse.setEmail(response.successResponse.user.email);
                signupResponse.setUsername(response.successResponse.user.username);
                signupResponse.setId(String.valueOf(response.successResponse.user.id));

                return signupResponse;
            }

            throw  resolveException(response);

        }

        throw new BadRequestException("Email is not valid");
    }

    private boolean validateEmail(String email) {
        EmailValidator validator = EmailValidator.getInstance();
        return validator.isValid(email);
    }

    @Override
    public SortedSet<String> getUserRolesFromApp(String appId, String userId) {
        ClientResponse<RegistrationResponse, Errors> response = fusionAuthClient.retrieveRegistration(UUID.fromString(userId), UUID.fromString(appId));

        if(response.wasSuccessful()) {
            return response.successResponse.registration.roles;
        }

        throw resolveException(response);
     }

    @Override
    public void deleteUserById(String userId) {



    }

    @Override
    public User getUserByUsername(String username) {
        ClientResponse<UserResponse, Errors> response = fusionAuthClient.retrieveUserByUsername(username);

        if(response.wasSuccessful()) {
            return response.successResponse.user;
        }

        throw resolveException(response);
    }

    @Override
    public User getUserInfoByJWT(String jwt) {

        ClientResponse<UserResponse, Errors> response = fusionAuthClient.retrieveUserUsingJWT(jwt);

        if (response.wasSuccessful()) {
            return response.successResponse.user;
        }
        throw  resolveException(response);

    }

    private RuntimeException resolveException(ClientResponse<?, Errors> response) {
        if (response.exception != null) {
            return new RuntimeException(response.exception);
        } else if (response.errorResponse != null) {
            return new BadRequestException(response.errorResponse.toString());
        } else {
            if (response.status == 400) {
                return new BadRequestException("The request was invalid and/or malformed.");
            } else if (response.status == 401) {
                return new UnauthorizedException("You did not supply a valid Authorization header.");
            } else if (response.status == 404) {
                return new NotFoundException("The user was not found or the password was incorrect.");
            } else if (response.status == 409) {
                return new ConflictException("The user is currently in an action that has prevented login.");
            } else if (response.status == 410) {
                return new GoneException("The user has expired.");
            } else if (response.status == 423) {
                return new LockedException("The user is locked and cannot login..");
            } else if (response.status == 500) {
                return new InternalServerErrorException("There was an internal error.");
            } else {
                return new ServiceUnavailableException(
                        "The search index is not available or encountered an exception so the request cannot be completed.");
            }
        }
    }
}
