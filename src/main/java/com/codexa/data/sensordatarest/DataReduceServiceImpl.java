package com.codexa.data.sensordatarest;

import com.codexa.data.sensordatarest.api.DataReduceService;
import com.codexa.data.sensordatarest.obj.SensorEntityQuery;
import lombok.Data;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Value;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by peterszatmary on 27/09/2017.
 */
@Log4j
@Data
public class DataReduceServiceImpl implements DataReduceService {


    @Value("${window.size}")
    private Long windowSize;


    @Override
    public List<SensorEntityQuery> reduce(List<SensorEntityQuery> dataToReduce) {

        if (dataToReduce.isEmpty()) {
            log.info("No reducing needed.");
            return dataToReduce;
        }

        log.info(String.format("Size before reducing = %d", dataToReduce.size()));
        log.info(String.format("Window size = %d", windowSize));

        int sendEach = (int)(long) calculateSendEach((long)dataToReduce.size());
        log.info(String.format("send each is = %d", sendEach));


        List<SensorEntityQuery> listToSend = new ArrayList();
        for (int i = 0; i < dataToReduce.size(); i = i + sendEach) {
            listToSend.add(dataToReduce.get(i));
        }

        log.info(String.format("Size after reducing = %d", listToSend.size()));
        return listToSend;
    }


    private Long calculateSendEach(Long dataCount) {
        return (windowSize >= dataCount || dataCount <= 0) ? 1L : dataCount / windowSize;
    }
}