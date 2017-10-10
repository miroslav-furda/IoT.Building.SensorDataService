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
@CrossOrigin
public class DataReduceController {

    @Autowired
    private DataReduceService dataReduceService;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private RestCall restCallImpl;




    @RequestMapping(value = "/ts/v1/query", method = POST)
    public @ResponseBody ResponseEntity<SensorDataContainer> reducedQuery(@RequestBody String selectQuery) {

        ResponseEntity<SensorDataContainer> sensorData = makeQueryCall(selectQuery, restCallImpl.query());

        SensorDataContainer responseBody = sensorData.getBody();
        List<SensorEntityQuery> reducedSensorData = dataReduceService.reduce(responseBody.getRows());
        responseBody.setRows(reducedSensorData);

        return sensorData;
    }


    @RequestMapping(
            value = "/ts/v1/tables/{table}/keys/deviceId/{deviceId}/type/{type}/time/{time}",
            method = DELETE)
    public @ResponseBody ResponseEntity<String> delete(
            @PathVariable String deviceId,
            @PathVariable String type,
            @PathVariable String time) {

        ResponseEntity<String> result;
        Map<String, Object> map = new HashMap<>();
        map.put("deviceId", deviceId);
        map.put("type", type);
        map.put("time", time);


        String call = restCallImpl.delete();

        try {
            restTemplate.delete(call, map); //rest client exc,
            result = new ResponseEntity<>("{\"success\":true}", HttpStatus.OK);
            log.info(String.format("delete call = %s", call));
        } catch (RuntimeException ex) {
            result = new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_MODIFIED);
        }
        return result;

    }

    @RequestMapping(
            value = "/ts/v1/tables/{table}/keys/deviceId/{deviceId}/type/{type}/time/{time}",
            method = GET)
    public @ResponseBody ResponseEntity<SensorEntityGet> get(
            @PathVariable String deviceId,
            @PathVariable String type,
            @PathVariable String time) {

        Map<String, Object> map = new HashMap<>();
        map.put("deviceId", deviceId);
        map.put("type", type);
        map.put("time", time);

        String call = restCallImpl.get();

        ResponseEntity<SensorEntityGet> response =
                restTemplate.getForEntity(call, SensorEntityGet.class, map);

        log.info(String.format("get call = %s", call));
        handleNoOK(call, response);

        return response;
    }

    @RequestMapping(
            value = "/ts/v1/tables/{table}/list_keys",
            method = GET)
    public @ResponseBody ResponseEntity<String> listKeys() {

        String call = restCallImpl.keys();

        ResponseEntity<String> response =
                restTemplate.getForEntity(call, String.class);

        log.info(String.format("keys call = %s", call));
        handleNoOK(call, response);

        return response;
    }


    @RequestMapping(
            value = "/ts/v1/tables/{table}/keys",
            method = POST)
    public @ResponseBody ResponseEntity<String> put(@RequestBody String jsonObjects) {

        ResponseEntity<String> result;

        String call = restCallImpl.put();

        try {
            makeQueryCall(jsonObjects, call);
            result = new ResponseEntity<>("{\"success\":true}", HttpStatus.OK);
            log.info(String.format("put call = %s", call));
        } catch (RuntimeException ex) {
            result = new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_MODIFIED);
        }

        return result;
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




    private ResponseEntity<SensorDataContainer> makeQueryCall(String sqlQuery, String restCall) {

        String decodedQuery = decodeQuery(sqlQuery);

        log.info("Raw sql query = " + sqlQuery);
        log.info("Decoded select query = " + decodedQuery);
        log.info("RestCallImpl rest call = " + restCall);

        HttpEntity<String> request = new HttpEntity<>(decodedQuery);
        ResponseEntity<String> response =
                restTemplate.exchange(restCall, HttpMethod.POST, request, String.class);

       handleNoOK(restCall, response);

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
    private void handleNoOK(String restCall, ResponseEntity<?> response) {
        if (!response.getStatusCode().equals(HttpStatus.OK)) {
            // nok
            log.error(String.format("Rest call %s end with HttpStatus = %d", response.getStatusCode(), restCall));
            response = new ResponseEntity(
                    String.format("error status with = %s", response.getStatusCode()), response.getStatusCode()
            );
        }
    }
}