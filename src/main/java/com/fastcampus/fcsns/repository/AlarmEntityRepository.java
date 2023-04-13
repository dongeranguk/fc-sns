package com.fastcampus.fcsns.repository;

import com.fastcampus.fcsns.model.entity.AlarmEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AlarmEntityRepository extends JpaRepository<AlarmEntity, Integer> {

    Page<AlarmEntity> findAllByUserId(@Param("user_id") Integer userId, Pageable pageable);
}
