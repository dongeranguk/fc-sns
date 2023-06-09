package com.fastcampus.fcsns.repository;

import com.fastcampus.fcsns.model.entity.LikeEntity;
import com.fastcampus.fcsns.model.entity.PostEntity;
import com.fastcampus.fcsns.model.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface LikeEntityRepository extends JpaRepository<LikeEntity, Integer> {

    Optional<LikeEntity> findByUserAndPost(UserEntity user, PostEntity post);

    Long countByPost(PostEntity post);

    List<LikeEntity> findAllByPost(PostEntity post);

    @Modifying
    @Transactional
    @Query(value = "UPDATE LikeEntity entity SET deleted_at = now() WHERE entity.post = :post")
    void deleteAllByPost(@Param("post") PostEntity postEntity);
}
