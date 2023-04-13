package com.fastcampus.fcsns.repository;

import com.fastcampus.fcsns.model.entity.CommentEntity;
import com.fastcampus.fcsns.model.entity.PostEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface CommentEntityRepository extends JpaRepository<CommentEntity, Integer> {

    Page<CommentEntity> findAllByPost(PostEntity post, Pageable pageable);

    @Modifying
    @Transactional
    @Query(value = "UPDATE CommentEntity entity SET deleted_at = now() WHERE entity.post = :post")
    void deleteAllByPost(@Param("post") PostEntity postEntity);

}
