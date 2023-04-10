package com.fastcampus.fcsns.controller.response;

import com.fastcampus.fcsns.model.Comment;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.sql.Timestamp;

@Getter
@AllArgsConstructor
public class CommentResponse {
    private Integer id;

    private String comment;

    private Integer postId;

    private String userName;

    private Timestamp registeredAt;

    private Timestamp updatedAt;

    private Timestamp deletedAt;

    public static CommentResponse fromComment(Comment comment) {
        return new CommentResponse(
                comment.getId(),
                comment.getComment(),
                comment.getPostId(),
                comment.getUserName(),
                comment.getRegisteredAt(),
                comment.getUpdatedAt(),
                comment.getDeleted_at()
        );
    }
}
