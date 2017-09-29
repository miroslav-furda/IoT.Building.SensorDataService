package com.codexa.data.sensordatarest.obj;

import lombok.Data;

import java.util.List;

/**
 * Created by peterszatmary on 28/09/2017.
 * Object that is holding data read from Riak TS db.
 */
@Data
public class SensorDataContainer {

    private List<String> columns;
    private List<SensorEntity> rows;
}