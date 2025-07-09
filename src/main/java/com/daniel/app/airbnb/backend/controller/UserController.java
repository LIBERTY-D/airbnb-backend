package com.daniel.app.airbnb.backend.controller;


import com.daniel.app.airbnb.backend.annotations.BearerToken;
import com.daniel.app.airbnb.backend.dto.LoginDto;
import com.daniel.app.airbnb.backend.dto.UpdateUserDTO;
import com.daniel.app.airbnb.backend.dto.UserDto;
import com.daniel.app.airbnb.backend.jwt.JwtService;
import com.daniel.app.airbnb.backend.res.HttpResponse;
import com.daniel.app.airbnb.backend.service.UserService;
import com.daniel.app.airbnb.backend.util.ResponseUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class UserController extends BaseController {

    private final UserService userService;

    private final JwtService jwtService;
    @Value("${custom.redirect-verify}")
    private String redirectVerify;
    private final ObjectMapper objectMapper;

    @GetMapping("users")
    public ResponseEntity<HttpResponse<Object>> getUsers() {

        var users = userService.getUsers();
        return ResponseEntity.ok(ResponseUtil.getResponse(users, null,
                "fetched " +
                        "users", HttpStatus.OK));
    }

    @GetMapping("users/{userId}")
    public ResponseEntity<HttpResponse<Object>> getUser(@PathVariable long userId) {
        var user = userService.getUser(userId);
        return new ResponseEntity<>(ResponseUtil.getResponse(user, null,
                "user details", HttpStatus.OK),
                HttpStatus.OK);

    }

    @PostMapping("users/register")
    public ResponseEntity<HttpResponse<Object>> registerUser(@Valid @RequestBody UserDto userDto) {
        var user = userService.createUser(userDto);
        return new ResponseEntity<>(ResponseUtil.getResponse(user, null,
                "user registered", HttpStatus.CREATED), HttpStatus.CREATED);
    }

    @GetMapping("users/auth")
    public ResponseEntity<HttpResponse<Object>> loggedInUser() {
        var user = userService.authenticatedUser();
        return new ResponseEntity<>(ResponseUtil.getResponse(user,
                null, "authenticated user", HttpStatus.OK), HttpStatus.OK);
    }

    @PostMapping("users/login")
    public ResponseEntity<HttpResponse<Object>> loginUser(@Valid @RequestBody LoginDto loginDto) {
        var user = userService.loginUser(loginDto);
        var accessToken = jwtService.generateAccessToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        var data = Map.of("access_token", accessToken, "refresh_token", refreshToken);

        return new ResponseEntity<>(ResponseUtil.getResponse(data, null,
                "user login success", HttpStatus.OK),
                HttpStatus.OK);
    }

    @PatchMapping("users")
    public ResponseEntity<HttpResponse<Object>> updateUser(@RequestBody UpdateUserDTO updateUserDTO) {
        var user = userService.updateUser(updateUserDTO);
        return new ResponseEntity<>(ResponseUtil.getResponse(user, null,
                "user updated success", HttpStatus.OK), HttpStatus.OK);
    }

    @DeleteMapping("users")
    public ResponseEntity<HttpResponse<Object>> deleteUserAccount() {
        userService.deleteUser(null);
        return new ResponseEntity<>(ResponseUtil.getResponse(null, null,
                "user deleted successfully", HttpStatus.OK), HttpStatus.OK);
    }

    @GetMapping("users/verify")
    public void veryUser(@RequestParam("token") String token,
                         HttpServletResponse res) throws IOException {
        userService.verifyUserByToken(token);
        res.sendRedirect(redirectVerify);

    }


    @GetMapping("users/refresh/token")
    public void getRefreshToken(@BearerToken String refreshToken, HttpServletResponse response) throws IOException {
        Map<String, String> tokens = userService.userTokens(refreshToken);
        objectMapper.writeValue(response.getOutputStream(),
                ResponseUtil.getResponse(tokens, null,
                        "token refresh", HttpStatus.OK));

        response.getOutputStream().flush();
    }
}
