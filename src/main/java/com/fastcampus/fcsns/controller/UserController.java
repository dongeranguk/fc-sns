package com.fastcampus.fcsns.controller;

import com.fastcampus.fcsns.controller.request.UserJoinRequest;
import com.fastcampus.fcsns.controller.request.UserLoginRequest;
import com.fastcampus.fcsns.controller.response.Response;
import com.fastcampus.fcsns.controller.response.UserJoinResponse;
import com.fastcampus.fcsns.controller.response.UserLoginResponse;
import com.fastcampus.fcsns.model.User;
import com.fastcampus.fcsns.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // TODO : implement
    @PostMapping("/join")
    public Response<UserJoinResponse> join(@RequestBody UserJoinRequest request) {
        User user = userService.join(request.getUserName(), request.getPassword());
        UserJoinResponse response = UserJoinResponse.fromUser(user);

        return Response.success(response);
    }

    @PostMapping("/login")
    public Response<UserLoginResponse> login(@RequestBody UserLoginRequest request) {
        String token = userService.login(request.getUserName(), request.getPassword());

        return Response.success(new UserLoginResponse(token));
    }
}
