package com.codexa.data.sensordatarest;

import com.codexa.data.sensordatarest.api.DataReduceService;
import com.codexa.data.sensordatarest.api.RestCall;
import com.codexa.data.sensordatarest.obj.SensorDataContainer;
import com.codexa.data.sensordatarest.obj.SensorEntityGet;
import com.codexa.data.sensordatarest.obj.SensorEntityQuery;
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

import static org.springframework.web.bind.annotation.RequestMethod.GET;
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

        ResponseEntity<SensorDataContainer> sensorData = makeQueryCall(selectQuery);

        SensorDataContainer responseBody = sensorData.getBody();
        List<SensorEntityQuery> reducedSensorData = dataReduceService.reduce(responseBody.getRows());
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

    @RequestMapping(
            value = "/ts/v1/tables/{table}/keys/deviceId/{deviceId}/type/{type}/time/{time}",
            method = GET)
    public @ResponseBody ResponseEntity<SensorEntityGet> get(
            @PathVariable String table,
            @PathVariable String deviceId,
            @PathVariable String type,
            @PathVariable String time) {

        Map<String, Object> map = new HashMap<>();
        map.put("deviceId", deviceId);
        map.put("type", type);
        map.put("time", time);

        ResponseEntity<SensorEntityGet> response =
                restTemplate.getForEntity(restCallUtil.get(), SensorEntityGet.class, map);
        handleExceptions(restCallUtil.get(), response);

        return response;
    }

    @RequestMapping(
            value = "/ts/v1/tables/{table}/list_keys",
            method = GET)
    public @ResponseBody ResponseEntity<String> listKeys(@PathVariable String table) {

        ResponseEntity<String> response =
                restTemplate.getForEntity(restCallUtil.keys(), String.class);
        handleExceptions(restCallUtil.keys(), response);

        return response;
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




    private ResponseEntity<SensorDataContainer> makeQueryCall(String sqlQuery) {

        String decodedQuery = decodeQuery(sqlQuery);
        String queryRestCall = restCallUtil.query();

        log.info("Raw sql query = " + sqlQuery);
        log.info("Decoded select query = " + decodedQuery);
        log.info("RestCallImpl rest call = " + queryRestCall);

        HttpEntity<String> request = new HttpEntity<>(decodedQuery);
        ResponseEntity<String> response =
                restTemplate.exchange(queryRestCall, HttpMethod.POST, request, String.class);

       handleExceptions(queryRestCall, response);

        ObjectMapper m = new ObjectMapper();
        SensorDataContainer result = null;
        try {
            result = m.readValue(response.getBody(), SensorDataContainer.class);
        } catch (IOException e) {
            log.error(e);
        }
        return new ResponseEntity<>(result, response.getStatusCode());
    }



    //TODO test
    private void handleExceptions(String restCall, ResponseEntity<?> response) {
        if (!response.getStatusCode().equals(HttpStatus.OK)) {
            // nok
            log.error(String.format("Rest call %s end with HttpStatus = %d", response.getStatusCode(), restCall));
            response = new ResponseEntity("error", response.getStatusCode());
        }
    }
}