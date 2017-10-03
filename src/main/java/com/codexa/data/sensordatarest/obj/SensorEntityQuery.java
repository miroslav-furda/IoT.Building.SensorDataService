package com.codexa.data.sensordatarest.obj;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.List;

/**
 * Created by peterszatmary on 27/09/2017.
 * Model object for sensor data. For query with many data.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SensorEntityQuery {

    private String deviceId;
    private String type;
    private Timestamp time;
    private String value;

    /**
     * This is hre because of unnatural format of json returned from RIAK TS with [ [], [], [] ] instead fo array of objects (rows)
     * @param valuesList
     */
    @JsonCreator
    public SensorEntityQuery(final List<Object> valuesList) {

        deviceId = (String) valuesList.get(0);
        type = (String) valuesList.get(1);
        time = new Timestamp((long)(int) valuesList.get(3));
        value = (String) valuesList.get(2);
    }
}