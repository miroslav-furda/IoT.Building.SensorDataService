package com.codexa.data.sensordatarest;

import com.codexa.data.sensordatarest.api.DataReducerService;
import com.codexa.data.sensordatarest.obj.SensorEntity;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Value;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by peterszatmary on 27/09/2017.
 */
@Log4j
public class DataReducerServiceImpl implements DataReducerService {

    //TODO test long int with many
    //TODO test reducing


    @Value("${window.size.in.points}")
    private Long windowSizeInPoints;

    @Override
    public List reduce(List<SensorEntity> dataToReduce) {

        log.info("reducer works. WINDOWS SIZE = " + windowSizeInPoints);
        int sendEach = (int)(long) calculateSendEach((long)dataToReduce.size());
        log.info("send each is = " + sendEach);

        List listToSend = new ArrayList();

        if (dataToReduce.isEmpty()) {
            return dataToReduce;
        }

        for (int i = 0; i < dataToReduce.size(); i = i + sendEach) {
            listToSend.add(dataToReduce.get(i));
        }
        return listToSend;
    }

    // curl -XPOST http://127.0.0.1:8098/ts/v1/query --data "SELECT * from SensorData WHERE
    // deviceId = 'foo' AND time > 1506253923 AND time < 1506426723"


    private Long calculateSendEach(Long dataCount) {
        if (dataCount <= 0) {
            return -1L;
        }
        return (windowSizeInPoints >= dataCount) ? 1L : dataCount / windowSizeInPoints;
    }
}