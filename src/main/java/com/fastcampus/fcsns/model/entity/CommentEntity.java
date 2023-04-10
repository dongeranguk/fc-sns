package com.fastcampus.fcsns.model.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.Instant;

@Getter
@Setter
@Where(clause = "deleted_at is null")
@SQLDelete(sql = "UPDATE \"comment\" SET deleted_at = now() WHERE id = ?")
@Table(name = "\"comments\"", indexes = {
        @Index(name = "post_id_idx", columnList = "post_id")
})
@Entity
public class CommentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private PostEntity post;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @Column(name = "comment")
    private String comment;

    @Column(name = "registered_at")
    private Timestamp registered_at;

    @Column(name = "updated_at")
    private Timestamp updated_at;

    @Column(name = "deleted_at")
    private Timestamp deleted_at;

    @PrePersist
    void registered_at() { this.registered_at = Timestamp.from(Instant.now()); }

    @PreUpdate
    void updated_at() { this.updated_at = Timestamp.from(Instant.now()); }

    public static CommentEntity of(UserEntity userEntity, PostEntity postEntity, String comment) {
        CommentEntity commentEntity = new CommentEntity();

        commentEntity.setUser(userEntity);
        commentEntity.setPost(postEntity);
        commentEntity.setComment(comment);

        return commentEntity;
    }

}
