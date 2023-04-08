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
@Entity
@Table(name="\"like\"")
@Where(clause = "deleted_at is null")
@SQLDelete(sql = "UPDATE \"like\" SET deleted_at = NOW() WHERE id = ?")
public class LikeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private PostEntity post;

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

    public static LikeEntity of(UserEntity userEntity, PostEntity postEntity) {
        LikeEntity entity = new LikeEntity();

        entity.setUser(userEntity);
        entity.setPost(postEntity);

        return entity;
    }
}
