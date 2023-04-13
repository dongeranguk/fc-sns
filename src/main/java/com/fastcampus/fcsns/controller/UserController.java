package com.fastcampus.fcsns.controller;

import com.fastcampus.fcsns.controller.request.UserJoinRequest;
import com.fastcampus.fcsns.controller.request.UserLoginRequest;
import com.fastcampus.fcsns.controller.response.AlarmResponse;
import com.fastcampus.fcsns.controller.response.Response;
import com.fastcampus.fcsns.controller.response.UserJoinResponse;
import com.fastcampus.fcsns.controller.response.UserLoginResponse;
import com.fastcampus.fcsns.exception.ErrorCode;
import com.fastcampus.fcsns.exception.SnsApplicationException;
import com.fastcampus.fcsns.model.User;
import com.fastcampus.fcsns.service.UserService;
import com.fastcampus.fcsns.util.ClassUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // TODO : implement
    @PostMapping("/join")
    public Response<UserJoinResponse> join(@RequestBody UserJoinRequest request) {
        User user = userService.join(request.getName(), request.getPassword());
        UserJoinResponse response = UserJoinResponse.fromUser(user);

        return Response.success(response);
    }

    @PostMapping("/login")
    public Response<UserLoginResponse> login(@RequestBody UserLoginRequest request) {
        String token = userService.login(request.getName(), request.getPassword());

        return Response.success(new UserLoginResponse(token));
    }

    @GetMapping("/alarm")
    public Response<Page<AlarmResponse>> alarm(Authentication authentication, Pageable pageable) {

        User user = ClassUtils.getSafeCastInstance(authentication.getPrincipal(), User.class).orElseThrow(() ->
                new SnsApplicationException(ErrorCode.INTERNAL_SERVER_ERROR, "Casting to User class failed"));

        return Response.success(userService.alarmList(user.getId(), pageable).map(AlarmResponse::fromAlarm));

    }
}
