package com.codexa.data.sensordatarest.obj;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

/**
 * Created by peterszatmary on 27/09/2017.
 * Model object for sensor data. For query with many data.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SensorEntityGet {

    private String deviceId;
    private String type;
    private Timestamp time;
    private String value;
}