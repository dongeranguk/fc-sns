package com.fastcampus.fcsns.controller;

import com.fastcampus.fcsns.controller.request.UserJoinRequest;
import com.fastcampus.fcsns.controller.request.UserLoginRequest;
import com.fastcampus.fcsns.exception.ErrorCode;
import com.fastcampus.fcsns.exception.SnsApplicationException;
import com.fastcampus.fcsns.model.User;
import com.fastcampus.fcsns.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @Test
    public void 회원가입() throws Exception {
        // Given
        String userName = "userName";
        String password = "password";

        // When & Then

        // TODO : mocking
        when(userService.join(userName, password)).thenReturn(mock(User.class));

        mvc.perform(post("/api/v1/users/join")
                .contentType(MediaType.APPLICATION_JSON)
                // TODO : add request body
                .content(objectMapper.writeValueAsBytes(new UserJoinRequest(userName, password)))
        ).andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void 회원가입시_이미_회원가입된_userName으로_회원가입을_하는경우_에러반환() throws Exception {
        // Given
        String userName = "userName";
        String password = "password";

        // When & Then

        // TODO : develop

        when(userService.join(userName, password)).thenThrow(new SnsApplicationException(ErrorCode.DUPLICATED_USER_NAME, ""));

        mvc.perform(post("/api/v1/users/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        // TODO : add request body
                        .content(objectMapper.writeValueAsBytes(new UserJoinRequest(userName, password)))
                ).andDo(print())
                .andExpect(status().isConflict());
    }

    @Test
    public void 로그인() throws Exception {
        // Given
        String userName = "userName";
        String password = "password";

        // When & Then
        when(userService.login(userName, password)).thenReturn("test_token");

        mvc.perform(post("/api/v1/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(new UserLoginRequest(userName, password)))
        ).andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void 로그인시_존재하지않는_userName으로_로그인하는경우_에러반환() throws Exception {
        // Given
        String userName = "notExistsUserName";
        String password = "password";

        // When & Then
        when(userService.login(userName, password)).thenThrow(new SnsApplicationException(ErrorCode.DUPLICATED_USER_NAME, ""));

        mvc.perform(post("/api/v1/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(new UserLoginRequest(userName, password)))
        ).andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void 로그인시_틀린_password를_입력할경우_에러반환() throws Exception {
        // Given
        String userName = "userName";
        String password = "inCorrectPassword";

        // When & Then
        when(userService.login(userName, password)).thenThrow(new SnsApplicationException(ErrorCode.DUPLICATED_USER_NAME, ""));

        mvc.perform(post("/api/v1/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(new UserLoginRequest(userName, password)))
        ).andDo(print())
                .andExpect(status().isUnauthorized());
    }


}
