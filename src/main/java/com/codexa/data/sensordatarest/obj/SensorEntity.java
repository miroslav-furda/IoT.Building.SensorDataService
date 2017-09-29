package com.codexa.data.sensordatarest.obj;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.List;

/**
 * Created by peterszatmary on 27/09/2017.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SensorEntity {

    private String deviceId;
    private String type;
    private Timestamp time;
    private String value;

    @JsonCreator
    public SensorEntity(final List<Object> valuesList) {

        deviceId = (String) valuesList.get(0);
        type = (String) valuesList.get(1);
        time = new Timestamp((long)(int) valuesList.get(3));
        value = (String) valuesList.get(2);
    }
}