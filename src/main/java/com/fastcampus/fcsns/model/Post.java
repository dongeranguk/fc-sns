package com.fastcampus.fcsns.model;

import com.fastcampus.fcsns.model.entity.PostEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.sql.Timestamp;

@Getter
@AllArgsConstructor
public class Post {

    private Integer postId;

    private String title;

    private String body;

    private User user;

    private Timestamp registered_at;

    private Timestamp updated_at;

    private Timestamp deleted_at;

    public static Post fromEntity(PostEntity entity) {
        return new Post(
                entity.getId(),
                entity.getTitle(),
                entity.getBody(),
                User.fromEntity(entity.getUser()),
                entity.getRegistered_at(),
                entity.getUpdated_at(),
                entity.getDeleted_at()
        );
    }
}
