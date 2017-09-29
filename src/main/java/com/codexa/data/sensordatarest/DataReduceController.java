package com.codexa.data.sensordatarest;

import com.codexa.data.sensordatarest.api.DataReduceService;
import com.codexa.data.sensordatarest.obj.SensorDataContainer;
import com.codexa.data.sensordatarest.obj.SensorEntity;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;

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

    @Value("${data.endpoint}")
    private String dataEndpoint;


    @RequestMapping(value = "/ts/v1/query", method = POST)
    public @ResponseBody ResponseEntity<SensorDataContainer> reduceDataFromQuery(@RequestBody String selectQuery) {

        ResponseEntity<SensorDataContainer> sensorData = makeCall(selectQuery);

        SensorDataContainer responseBody = sensorData.getBody();
        List<SensorEntity> reducedSensorData = dataReduceService.reduce(responseBody.getRows());
        responseBody.setRows(reducedSensorData);

        return sensorData;
    }


    public ResponseEntity delete() {
        throw new UnsupportedOperationException(); //TODO
    }


    public ResponseEntity get() {
        throw new UnsupportedOperationException(); //TODO
    }

    public ResponseEntity listKeys() {
        throw new UnsupportedOperationException(); //TODO
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

    private String createHTTPCall() {

        return
                new StringBuilder()
                .append(dataEndpoint)
                .append("/ts/v1/query")
                .toString();
    }


    private ResponseEntity<SensorDataContainer> makeCall(String query) {

        String decodedQuery = decodeQuery(query);
        String queryRestCall = createHTTPCall();

        log.info("Raw query = " + query);
        log.info("Decoded select query = " + decodedQuery);
        log.info("Query rest call = " + queryRestCall);

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