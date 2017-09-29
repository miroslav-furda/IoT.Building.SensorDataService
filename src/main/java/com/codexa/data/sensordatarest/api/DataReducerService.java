package com.codexa.data.sensordatarest.api;

import com.codexa.data.sensordatarest.obj.SensorEntity;

import java.util.List;

/**
 * Created by peterszatmary on 27/09/2017.
 */
public interface DataReducerService {
    List reduce(List<SensorEntity> dataToReduce);
}