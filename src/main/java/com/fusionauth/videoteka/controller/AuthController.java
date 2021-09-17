package com.fusionauth.videoteka.controller;

import com.fusionauth.videoteka.payload.request.SigninRequest;
import com.fusionauth.videoteka.payload.request.SignupRequest;
import com.fusionauth.videoteka.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/admin")
    public ResponseEntity<?> createAdmin(@RequestBody SignupRequest signupRequest) {
        return new ResponseEntity<>(authService.createAdmin(signupRequest), HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<?> signupUser (@RequestBody SignupRequest signupRequest) {
        return new ResponseEntity<>(authService.signupResponse(signupRequest), HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<?> signInUser(@RequestBody SigninRequest signinRequest) {
        return new ResponseEntity<>(authService.signinResponse(signinRequest), HttpStatus.OK);
    }

    @GetMapping("/user")
    public ResponseEntity<?> getUserByJwt(@RequestParam String jwt) {
        return new ResponseEntity<>(authService.getUserByJwt(jwt), HttpStatus.OK);
    }

    @GetMapping("/user/{username}")
    public ResponseEntity<?> getUserByUsername(@PathVariable String username) {
        return new ResponseEntity<>(authService.getUserByUsername(username), HttpStatus.OK);
    }

    @GetMapping("/user/roles")
    public ResponseEntity<?> getUserRolesFromApp(@RequestParam String appId, @RequestParam String userId) {
        return new ResponseEntity<>(authService.getUserRolesFromApp(appId, userId), HttpStatus.OK);
    }

    @GetMapping("/jwt/validate")
    public ResponseEntity<?> validateJwt(@RequestParam String jwt){
        return new ResponseEntity<>(authService.validateJwt(jwt), HttpStatus.OK);
    }
}
