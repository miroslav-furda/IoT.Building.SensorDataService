package com.codexa.data.sensordatarest;

import com.codexa.data.sensordatarest.api.DataReduceService;
import com.codexa.data.sensordatarest.api.RestCall;
import com.codexa.data.sensordatarest.util.RestCallImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * Created by peterszatmary on 27/09/2017.
 */
@Configuration
public class Beans {

    @Bean
    public DataReduceService dataReducerService() {
        return new DataReduceServiceImpl();
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public RestCall restCall(@Value("${table.name}") String tableName) {
        return new RestCallImpl(tableName);
    }
}