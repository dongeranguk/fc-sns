package com.fastcampus.fcsns.controller;

import com.fastcampus.fcsns.controller.request.CommentRequest;
import com.fastcampus.fcsns.controller.request.PostCreateRequest;
import com.fastcampus.fcsns.controller.request.PostModifyRequest;
import com.fastcampus.fcsns.exception.ErrorCode;
import com.fastcampus.fcsns.exception.SnsApplicationException;
import com.fastcampus.fcsns.fixture.PostEntityFixture;
import com.fastcampus.fcsns.fixture.UserEntityFixture;
import com.fastcampus.fcsns.model.Post;
import com.fastcampus.fcsns.model.entity.UserEntity;
import com.fastcampus.fcsns.service.PostService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
public class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PostService postService;

    @Test
    @WithMockUser
    public void 포스트작성_정상호출() throws Exception {
        // Given
        String title = "title";
        String body = "body";

        // When
        mockMvc.perform(post("/api/v1/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new PostCreateRequest(title, body)))
                ).andDo(print())
                .andExpect(status().isOk());

        // Then
    }

    @Test
    @WithAnonymousUser
    public void 포스트작성시_로그인하지않은경우_에러반환() throws Exception {
        // Given
        String title = "title";
        String body = "body";

        // When
        mockMvc.perform(post("/api/v1/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new PostCreateRequest(title, body)))
                ).andDo(print())
                .andExpect(status().isUnauthorized());

        // Then
    }

    @Test
    @WithMockUser
    public void 포스트수정_정상호출() throws Exception {
        // Given
        String title = "title";
        String body = "body";
        Integer postId = 1;

        when(postService.modify(eq(title), eq(body), any(), any()))
                .thenReturn(Post.fromEntity(PostEntityFixture.get("userName", 1, 1)));

        // When
        mockMvc.perform(put("/api/v1/posts/" + postId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new PostModifyRequest(title, body)))
                ).andDo(print())
                .andExpect(status().isOk());

        // Then
    }

    @Test
    @WithAnonymousUser
    public void 포스트수정시_로그인하지_않은경우() throws Exception {
        // Given
        String title = "title";
        String body = "body";
        Integer postId = 1;

        // When
        mockMvc.perform(put("/api/v1/posts/" + postId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new PostModifyRequest(title, body)))
                ).andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithAnonymousUser
    public void 포스트수정시_본인이작성한글이아니라면_에러반환() throws Exception {
        // Given
        String title = "title";
        String body = "body";
        Integer postId = 1;

        // When
        doThrow(new SnsApplicationException(ErrorCode.INVALID_PERMISSION)).when(postService).modify(eq(title), eq(body), any(), eq(postId));

        mockMvc.perform(put("/api/v1/posts/" + postId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new PostModifyRequest(title, body)))
                ).andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    public void 포스트수정시_수정할게시글이존재하지않는경우_에러반환() throws Exception {
        // Given
        String title = "title";
        String body = "body";
        Integer postId = -1;

        // When
        doThrow(new SnsApplicationException(ErrorCode.POST_NOT_FOUND)).when(postService).modify(eq(title), eq(body), any(), eq(postId));

        mockMvc.perform(put("/api/v1/posts/" + postId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new PostModifyRequest(title, body)))
                ).andDo(print())
                .andExpect(status().isNotFound());

        // Then
    }

    @Test
    @WithMockUser
    public void 포스트삭제_정상호출() throws Exception {
        // Given
        Integer postId = 1;

        // When & Then
        mockMvc.perform(delete("/api/v1/posts/" + postId)
                        .contentType(MediaType.APPLICATION_JSON)
                ).andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithAnonymousUser
    public void 포스트삭제시_로그인하지않은경우_에러반환() throws Exception {
        // Given
        Integer postId = 1;

        // When & Then
        mockMvc.perform(delete("/api/v1/posts" + postId)
                        .contentType(MediaType.APPLICATION_JSON)
                ).andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithAnonymousUser
    public void 포스트삭제시_본인이작성한글이아니라면_에러반환() throws Exception {
        // Given
        Integer postId = 1;

        // When & Then
        doThrow(new SnsApplicationException(ErrorCode.INVALID_PERMISSION)).when(postService).delete(any(), any());

        mockMvc.perform(delete("/api/v1/posts" + postId)
                        .contentType(MediaType.APPLICATION_JSON)
                ).andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    public void 포스트삭제시_삭제할글이없다면_에러반환() throws Exception {
        // Given
        Integer postId = 1;

        // When & Then
        doThrow(new SnsApplicationException(ErrorCode.POST_NOT_FOUND)).when(postService).delete(any(), any());

        mockMvc.perform(delete("/api/v1/posts" + postId)
                        .contentType(MediaType.APPLICATION_JSON)
                ).andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    public void 피드목록요청시_정상호출() throws Exception {
        // When

        when(postService.list(any())).thenReturn(Page.empty());

        mockMvc.perform(get("/api/v1/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                ).andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithAnonymousUser
    public void 피드목록요청시_로그인하지않은경우_에러반환() throws Exception {
        // When

        when(postService.list(any())).thenReturn(Page.empty());

        mockMvc.perform(get("/api/v1/posts")
                .contentType(MediaType.APPLICATION_JSON)
        ).andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    public void 내피드목록요청시_정상호출() throws Exception {
        // When

        when(postService.my(any(), any())).thenReturn(Page.empty());

        mockMvc.perform(get("/api/v1/posts/my")
                .contentType(MediaType.APPLICATION_JSON)
        ).andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithAnonymousUser
    public void 내피드목록요청시_로그인하지않은경우_에러반환() throws Exception {
        // When

        when(postService.my(any(), any())).thenReturn(Page.empty());

        mockMvc.perform(get("/api/v1/posts/my")
                .contentType(MediaType.APPLICATION_JSON)
        ).andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    public void 좋아요클릭시_정상호출() throws Exception {
        // Given
        Integer postId = 1;

        // When
        mockMvc.perform(post("/api/v1/posts/" + postId + "/likes")
                .contentType(MediaType.APPLICATION_JSON)
        ).andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithAnonymousUser
    public void 좋아요클릭시_로그인하지않은경우_에러반환() throws Exception {
        // Given
        Integer postId = 1;
        UserEntity user = UserEntityFixture.empty();
        // When
        doThrow(new SnsApplicationException(ErrorCode.USER_NOT_FOUND)).when(postService).likes(postId, user.getUserName());

        mockMvc.perform(post("/api/v1/posts/" + postId + "/likes")
                .contentType(MediaType.APPLICATION_JSON)
        ).andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    public void 좋아요클릭시_게시글이없는경우_에러반환() throws Exception {
        // When
        // mock 데이터를 만들어 likes 메소드의 파라미터를 채워줬더니 예외가 발생하지 않아 기대한 값('404')와 다르게 나옴('200')
        // 따라서, 기대한 값 '404'를 결과로 받기 위해 any()로 대체해줌.
        doThrow(new SnsApplicationException(ErrorCode.POST_NOT_FOUND)).when(postService).likes(any(), any());

        mockMvc.perform(post("/api/v1/posts/1/likes")
                .contentType(MediaType.APPLICATION_JSON)
        ).andDo(print())
                .andExpect(status().isNotFound());

    }

    @Test
    @WithMockUser
    public void 댓글기능_정상호출() throws Exception {
        // Given
        Integer postId = 1;
        String comment = "comment";

        // When & Then
        mockMvc.perform(post("/api/v1/posts/ "+ postId + "/comments")
                .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new CommentRequest(comment)))
        ).andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithAnonymousUser
    public void 댓글작성시_로그인하지않은경우_에러반환() throws Exception {
        // Given
        Integer postId = 1;
        String userName = "userName";
        String comment = "comment";

        // When & Then
        doThrow(new SnsApplicationException(ErrorCode.USER_NOT_FOUND)).when(postService).comment(postId, userName, comment);

        mockMvc.perform(post("/api/v1/posts/" + postId + "/comments")
                .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new CommentRequest(comment)))
        ).andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    public void 댓글작성시_게시글이없는경우_에러반환() throws Exception {
        // Given
        Integer postId = 1;
        String userName = "userName";
        String comment = "comment";

        // When & Then
        doThrow(new SnsApplicationException(ErrorCode.POST_NOT_FOUND)).when(postService).comment(any(), any(), any());

        mockMvc.perform(post("/api/v1/posts" + postId + "/comments")
                .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new CommentRequest(comment)))
        ).andDo(print())
                .andExpect(status().isNotFound());
    }

}
