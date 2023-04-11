package com.fastcampus.fcsns.model;

import com.fastcampus.fcsns.model.entity.AlarmEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.sql.Timestamp;

@Getter
@AllArgsConstructor
public class Alarm {
    private Integer id;

    private User user;

    private AlarmType type;

    private AlarmArgs args;

    private Timestamp registeredAt;

    private Timestamp updatedAt;

    private Timestamp deletedAt;

    public static Alarm fromEntity(AlarmEntity entity) {
        return new Alarm(
                entity.getId(),
                User.fromEntity(entity.getUser()),
                entity.getAlarmType(),
                entity.getArgs(),
                entity.getRegisteredAt(),
                entity.getUpdatedAt(),
                entity.getDeletedAt()
        );
    }
}
