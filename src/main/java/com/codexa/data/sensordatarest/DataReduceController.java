package com.codexa.data.sensordatarest;

import com.codexa.data.sensordatarest.api.DataReduceService;
import com.codexa.data.sensordatarest.api.RestCall;
import com.codexa.data.sensordatarest.obj.SensorDataContainer;
import com.codexa.data.sensordatarest.obj.SensorEntity;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * Created by peterszatmary on 27/09/2017.
 */
@Log4j
@RestController
public class DataReduceController {

    @Autowired
    private DataReduceService dataReduceService;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private RestCall restCallUtil;




    @RequestMapping(value = "/ts/v1/query", method = POST)
    public @ResponseBody ResponseEntity<SensorDataContainer> reduceQueryCall(@RequestBody String selectQuery) {

        ResponseEntity<SensorDataContainer> sensorData = makeCall(selectQuery);

        SensorDataContainer responseBody = sensorData.getBody();
        List<SensorEntity> reducedSensorData = dataReduceService.reduce(responseBody.getRows());
        responseBody.setRows(reducedSensorData);

        return sensorData;
    }


    @RequestMapping(
            value = "/ts/v1/tables/{table}/keys/deviceId/{deviceId}/type/{type}/time/{time}",
            method = DELETE)
    public @ResponseBody ResponseEntity<String> delete(
            @PathVariable String table,
            @PathVariable String deviceId,
            @PathVariable String type,
            @PathVariable String time) {

        ResponseEntity<String> result;
        Map<String, Object> map = new HashMap<>();
        map.put("deviceId", deviceId);
        map.put("type", type);
        map.put("time", time);

        String deleteCall = restCallUtil.delete();

        try {
            restTemplate.delete(deleteCall, map); //rest client exc,
            result = new ResponseEntity<>("{\"success\":true}", HttpStatus.OK);
        } catch (RuntimeException ex) {
            result = new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_MODIFIED);
        }
        return result;

    }


    public ResponseEntity get() {
        throw new UnsupportedOperationException(); //TODO
    }

    public ResponseEntity listKeys() {
        throw new UnsupportedOperationException();
    }

    public ResponseEntity put() {
        throw new  UnsupportedOperationException(); //TODO
    }


    /**
     * special character should be decoded back. For example %27 back to ' or + to space.
     * @param query
     * @return
     */
    private String decodeQuery(String query) {

        String selectDecoded = null;
        try {
            selectDecoded = URLDecoder.decode(query, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            log.error(e);
        }
        return selectDecoded;
    }




    private ResponseEntity<SensorDataContainer> makeCall(String query) {

        String decodedQuery = decodeQuery(query);
        String queryRestCall = restCallUtil.query();

        log.info("Raw query = " + query);
        log.info("Decoded select query = " + decodedQuery);
        log.info("RestCallImpl rest call = " + queryRestCall);

        HttpEntity<String> request = new HttpEntity<>(decodedQuery);
        ResponseEntity<String> response =
                restTemplate.exchange(queryRestCall, HttpMethod.POST, request, String.class);

        if (!response.getStatusCode().equals(HttpStatus.OK)) {
            // nok
            log.error(String.format("Rest call end with HttpStatus = %d", response.getStatusCode()));
            return new ResponseEntity("error", response.getStatusCode());
        }

        ObjectMapper m = new ObjectMapper();
        SensorDataContainer result = null;
        try {
            result = m.readValue(response.getBody(), SensorDataContainer.class);
        } catch (IOException e) {
            log.error(e);
        }
        return new ResponseEntity<>(result, response.getStatusCode());
    }
}