package com.codexa.data.sensordatarest;

import com.codexa.data.sensordatarest.api.DataReduceService;

import com.codexa.data.sensordatarest.obj.SensorEntity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.*;

/**
 * Created by peterszatmary on 29/09/2017.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class DataReduceServiceImplTest {

    @Autowired
    private DataReduceService dataReduceService;



    @Test
    public void reduceTest() {

        assertNotNull(dataReduceService);

        List<SensorEntity> reduced = dataReduceService.reduce(noData());
        assertTrue(reduced.isEmpty());

        reduced = dataReduceService.reduce(data_100());
        assertFalse(reduced.isEmpty());
        assertEquals(100, reduced.size());

        reduced = dataReduceService.reduce(data_500());
        assertFalse(reduced.isEmpty());
        assertEquals(100, reduced.size());
    }




    private List<SensorEntity> noData() {
        return new ArrayList<>();
    }


    private List<SensorEntity> data_100() {
        return data(100);
    }

    private List<SensorEntity> data_500() {
       return data(500);
    }

    private List<SensorEntity> data(int size) {

        List<SensorEntity> result = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            result.add(getSensorEntity());
        }
        return result;
    }


    private SensorEntity getSensorEntity() {

        SensorEntity sensorEntity = new SensorEntity();
        sensorEntity.setDeviceId("deviceId-2");
        sensorEntity.setTime(new Timestamp(System.currentTimeMillis()));
        sensorEntity.setType("type-2");
        sensorEntity.setValue("value-" + (new Random().nextInt(1000)));
        return sensorEntity;
    }
}