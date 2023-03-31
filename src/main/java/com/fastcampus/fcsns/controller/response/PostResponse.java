package com.fastcampus.fcsns.controller.response;

import com.fastcampus.fcsns.model.Post;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.sql.Timestamp;

@Getter
@AllArgsConstructor
public class PostResponse {

    private Integer postId;

    private String title;

    private String body;

    private UserResponse user;

    private Timestamp registered_at;

    private Timestamp updated_at;

    private Timestamp deleted_at;


    public static PostResponse fromPost(Post post) {
        return new PostResponse(
                post.getPostId(),
                post.getTitle(),
                post.getBody(),
                UserResponse.fromUser(post.getUser()),
                post.getRegistered_at(),
                post.getUpdated_at(),
                post.getDeleted_at()
        );
    }
}
