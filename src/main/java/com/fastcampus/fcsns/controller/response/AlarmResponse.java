package com.fastcampus.fcsns.controller.response;

import com.fastcampus.fcsns.model.Alarm;
import com.fastcampus.fcsns.model.AlarmArgs;
import com.fastcampus.fcsns.model.AlarmType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.sql.Timestamp;

@Getter
@AllArgsConstructor
public class AlarmResponse {
    private Integer id;

    private AlarmType type;

    private AlarmArgs args;

    private String text;

    private Timestamp registeredAt;

    private Timestamp updatedAt;

    private Timestamp deletedAt;

    public static AlarmResponse fromAlarm(Alarm alarm) {
        return new AlarmResponse(
                alarm.getId(),
                alarm.getType(),
                alarm.getArgs(),
                alarm.getType().getAlarmText(),
                alarm.getRegisteredAt(),
                alarm.getUpdatedAt(),
                alarm.getDeletedAt()
        );
    }
}
