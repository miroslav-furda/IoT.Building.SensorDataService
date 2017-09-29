package com.codexa.data.sensordatarest;

import com.codexa.data.sensordatarest.api.DataReducerService;
import com.codexa.data.sensordatarest.obj.SensorDataContainer;
import com.codexa.data.sensordatarest.obj.SensorEntity;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
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
    private DataReducerService dataReducerService;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${data.endpoint}")
    private String dataEndpoint;


    @RequestMapping(value = "/ts/v1/query", method = POST)
    public @ResponseBody SensorDataContainer filterQueryCall(@RequestBody String selectQuery) {


        String decodedQuery = decodeQuery(selectQuery);
        String queryRestCall = createHTTPCall(decodedQuery);

        log.info("raw select query = " + selectQuery);
        log.info("decoded select query = " + decodedQuery);
        log.info("query rest call = " + queryRestCall);


        HttpEntity<String> request = new HttpEntity<>(decodedQuery);
        ResponseEntity<String> t = restTemplate.exchange(queryRestCall, HttpMethod.POST, request, String.class);

        ObjectMapper m = new ObjectMapper();
        SensorDataContainer xx = null;
        try {
            xx = m.readValue(t.getBody(), SensorDataContainer.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("xx = " + xx);


        // tt = {
        //      "columns":["deviceId","type","value","time"],
        //      "rows":[["foo","sit","amet",1506340047],["foo","bar","Lorem",1506340107]]}

        log.info("t = " + t.getStatusCode());



        log.info("t.getBody = " + t.getBody());
        log.info("size =" + xx.getRows().size());


        List<SensorEntity> res = dataReducerService.reduce(xx.getRows());
        log.info("reduce = " + res.size());
        xx.setRows(res);

        return xx;
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
            e.printStackTrace();
        }
        return selectDecoded;
    }

    private String createHTTPCall(String query) {

        return
                new StringBuilder()
                .append(dataEndpoint)
                .append("/ts/v1/query")
                .toString();
    }
}