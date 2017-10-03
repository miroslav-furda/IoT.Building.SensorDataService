package com.codexa.data.sensordatarest.api;

import com.codexa.data.sensordatarest.obj.SensorEntityQuery;

import java.util.List;

/**
 * Created by peterszatmary on 27/09/2017.
 */
public interface DataReduceService {

    /**
     * reducing algorithm for reducing data sent through the internet.
     * For avoiding backpressure.
     * @param dataToReduce
     * @return
     */
    List<SensorEntityQuery> reduce(List<SensorEntityQuery> dataToReduce);

    /**
     * Get window size. Its important for data reducer calculations.
     * If bigger more data can be send.
     * @return
     */
    Long getWindowSize();
}