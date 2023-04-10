package com.fastcampus.fcsns.model;

import com.fastcampus.fcsns.model.entity.CommentEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.sql.Timestamp;

@Getter
@AllArgsConstructor
public class Comment {
    private Integer id;

    private String comment;

    private Integer postId;

    private String userName;

    private Timestamp registeredAt;

    private Timestamp updatedAt;

    private Timestamp deleted_at;

    public static Comment fromEntity(CommentEntity entity) {
        return new Comment(
                entity.getId(),
                entity.getComment(),
                entity.getPost().getId(),
                entity.getUser().getUserName(),
                entity.getRegistered_at(),
                entity.getUpdated_at(),
                entity.getDeleted_at()
        );
    }


}
