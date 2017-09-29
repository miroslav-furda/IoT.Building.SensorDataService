package com.codexa.data.sensordatarest;

import com.codexa.data.sensordatarest.api.DataReduceService;
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
}