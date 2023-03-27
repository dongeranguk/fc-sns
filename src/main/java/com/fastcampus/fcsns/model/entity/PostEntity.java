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
@SQLDelete(sql = "UPDATE \"post\" SET deleted_at = NOW() where id = ?")
@Where(clause = "deleted_at is NULL")
@Table(name = "\"post\"")
@Entity
public class PostEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "title")
    private String title;

    @Column(name = "body", columnDefinition = "TEXT")
    private String body;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity userEntity;

    @Column(name = "created_at")
    private Timestamp created_at;

    @Column
    private Timestamp updated_at;

    @Column
    private Timestamp deleted_at;

    @PrePersist
    void created_at() {
        this.created_at = Timestamp.from(Instant.now());
    }

    @PreUpdate
    void updated_at() {
        this.updated_at = Timestamp.from(Instant.now());
    }
}
