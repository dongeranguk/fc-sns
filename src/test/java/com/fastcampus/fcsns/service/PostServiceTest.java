package com.fastcampus.fcsns.service;

import com.fastcampus.fcsns.exception.ErrorCode;
import com.fastcampus.fcsns.exception.SnsApplicationException;
import com.fastcampus.fcsns.fixture.PostEntityFixture;
import com.fastcampus.fcsns.fixture.UserEntityFixture;
import com.fastcampus.fcsns.model.entity.PostEntity;
import com.fastcampus.fcsns.model.entity.UserEntity;
import com.fastcampus.fcsns.repository.LikeEntityRepository;
import com.fastcampus.fcsns.repository.PostEntityRepository;
import com.fastcampus.fcsns.repository.UserEntityRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
public class PostServiceTest {

    @Autowired private PostService postService;

    @MockBean private PostEntityRepository postEntityRepository;

    @MockBean private UserEntityRepository userEntityRepository;

    @MockBean private LikeEntityRepository likeEntityRepository;

    @Test
    public void 포스트작성이_성공한경우() {
        // Given
        String title = "title";
        String body = "body";
        String userName = "userName";

        // When
        when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.of(mock(UserEntity.class)));
        when(postEntityRepository.save(any())).thenReturn(mock(PostEntity.class));

        // Then
        Assertions.assertDoesNotThrow(() -> postService.create(title, body, userName));
    }

    @Test
    public void 포스트작성시_요청한유저가_존재하지않는경우() {
        // Given
        String title = "title";
        String body = "body";
        String userName = "userName";

        // When
        when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.empty());
        when(postEntityRepository.save(any())).thenReturn(mock(PostEntity.class));

        // Then
        SnsApplicationException e = Assertions.assertThrows( SnsApplicationException.class, () -> postService.create(title, body, userName));
        Assertions.assertEquals(ErrorCode.USER_NOT_FOUND, e.getErrorCode());
    }

    @Test
    public void 포스트수정이_성공한경우() {
        // Given
        String title = "title";
        String body = "body";
        String userName = "userName";
        Integer postId = 1;

        PostEntity postEntity = PostEntityFixture.get(userName, postId, 1);
        UserEntity userEntity = postEntity.getUser();

        // When
        when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.of(userEntity));
        when(postEntityRepository.findById(postId)).thenReturn(Optional.of(postEntity));
        when(postEntityRepository.saveAndFlush(postEntity)).thenReturn(postEntity);

        // Then
        Assertions.assertDoesNotThrow(() -> postService.modify(title, body, userName, postId));
    }

    @Test
    public void 포스트수정시_포스트가존재하지않는경우() {
        // Given
        String title = "title";
        String body = "body";
        String userName = "userName";
        Integer postId = 1;

        PostEntity postEntity = PostEntityFixture.get(userName, postId, 1);
        UserEntity userEntity = postEntity.getUser();
        // When
        when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.of(userEntity));
        when(postEntityRepository.findById(postId)).thenReturn(Optional.empty());

        // Then
        SnsApplicationException e = Assertions.assertThrows(SnsApplicationException.class, () -> postService.modify(title, body, userName, postId));
        Assertions.assertEquals(ErrorCode.POST_NOT_FOUND, e.getErrorCode());
    }

    @Test
    public void 포스트수정시_권한이없는경우() {
        // Given
        String title = "title";
        String body = "body";
        String userName = "userName";
        Integer postId = 1;

        PostEntity postEntity = PostEntityFixture.get(userName, postId, 1);
        UserEntity writer = UserEntityFixture.get("userName", "password", 1);

        // When
        when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.of(writer));
        when(postEntityRepository.findById(postId)).thenReturn(Optional.of(postEntity));

        // Then
        SnsApplicationException e = Assertions.assertThrows(SnsApplicationException.class, () -> postService.modify(title, body, userName, postId));
        Assertions.assertEquals(ErrorCode.INVALID_PERMISSION, e.getErrorCode());
    }

    @Test
    void 포스트삭제가_성공한경우() {
        String userName = "userName";
        Integer postId = 1;

        PostEntity postEntity = PostEntityFixture.get(userName, postId, 1);
        UserEntity userEntity = postEntity.getUser();

        when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.of(userEntity));
        when(postEntityRepository.findById(postId)).thenReturn(Optional.of(postEntity));

        Assertions.assertDoesNotThrow(() -> postService.delete(userName, postId));
    }

    @Test
    void 포스트삭제시_포스트가존재하지않는경우() {
        String userName = "userName";
        Integer postId = 1;

        PostEntity postEntity = PostEntityFixture.get(userName, postId, 1);
        UserEntity userEntity = postEntity.getUser();

        when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.of(userEntity));
        when(postEntityRepository.findById(postId)).thenReturn(Optional.empty());

        SnsApplicationException e = Assertions.assertThrows(SnsApplicationException.class, () -> postService.delete("userName", 1));
        Assertions.assertEquals(ErrorCode.POST_NOT_FOUND, e.getErrorCode());
    }

    @Test
    void 포스트삭제시_권한이없는경우() {
        String userName = "userName";
        Integer postId = 1;

        PostEntity postEntity = PostEntityFixture.get(userName, postId, 1);
        UserEntity writer = UserEntityFixture.get("userName1", "password", 2);

        when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.of(writer));
        when(postEntityRepository.findById(postId)).thenReturn(Optional.of(postEntity));

        SnsApplicationException e = Assertions.assertThrows(SnsApplicationException.class, () -> postService.delete(userName, postId));
        Assertions.assertEquals(ErrorCode.INVALID_PERMISSION, e.getErrorCode());
    }

    @Test
    void 피드목록요청이_성공한경우() {
        // Given
        Pageable pageable = mock(Pageable.class);

        // When
        when(postEntityRepository.findAll(pageable)).thenReturn(Page.empty());

        // Then
        Assertions.assertDoesNotThrow(() -> postService.list(pageable));
    }

    @Test
    void 내피드목록요청이_성공한경우() {
        // Given
        Pageable pageable = mock(Pageable.class);
        UserEntity user = mock(UserEntity.class);

        // When
        when(userEntityRepository.findByUserName(any())).thenReturn(Optional.of(user));
        when(postEntityRepository.findAllByUser(user, pageable)).thenReturn(Page.empty());

        // Then
        Assertions.assertDoesNotThrow(() -> postService.my(pageable, ""));
    }

    @Test
    void 좋아요요청이_성공한경우() {
        // Given
        Integer postId = 1;
        UserEntity user = UserEntityFixture.get("userName", "password", 1);
        PostEntity post = PostEntityFixture.get(user.getUserName(), postId, user.getId());

        // When
        when(userEntityRepository.findByUserName(user.getUserName())).thenReturn(Optional.of(user));
        when(postEntityRepository.findById(postId)).thenReturn(Optional.of(post));
        when(likeEntityRepository.findByUserAndPost(user, post));

        // Then

    }
}
