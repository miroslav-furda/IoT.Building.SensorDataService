package com.codexa.data.sensordatarest.util;

import com.codexa.data.sensordatarest.api.RestCall;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Value;


/**
 * Created by peterszatmary on 02/10/2017.
 */
@Log4j
public class RestCallImpl implements RestCall {

    @Value("${data.endpoint}")
    private String dataEndpoint;

    @Value("${rest.version}")
    private String restVersion;

    private String tableName;

    public RestCallImpl(String tableName) {
        this.tableName = tableName;
    }

    @Override
    public String query() {

        String query =
                new StringBuilder()
                        .append(dataEndpoint)
                        .append("/ts/")
                        .append(restVersion)
                        .append("/query")
                        .toString();
        log.info(query);
        return query;
    }

    @Override
    public String delete() {

        String query =
        new StringBuilder()
                .append(dataEndpoint)
                .append("/ts/")
                .append(restVersion)
                .append("/tables/")
                .append(tableName)
                .append("/keys/deviceId/{deviceId}/type/{type}/time/{time}")
                .toString();

        log.info(query);
        return query;
    }

    @Override
    public String put() {
        String query =
                new StringBuilder()
                        .append(dataEndpoint)
                        .append("/ts/")
                        .append(restVersion)
                        .append("/tables/")
                        .append(tableName)
                        .append("/keys")
                        .toString();

        log.info(query);
        return query;
    }

    @Override
    public String get() {
        String query =
                new StringBuilder()
                        .append(dataEndpoint)
                        .append("/ts/")
                        .append(restVersion)
                        .append("/tables/")
                        .append(tableName)
                        .append("/keys/deviceId/{deviceId}/type/{type}/time/{time}")
                        .toString();

        log.info(query);
        return query;
    }

    @Override
    public String keys() {
        String query =
                new StringBuilder()
                        .append(dataEndpoint)
                        .append("/ts/")
                        .append(restVersion)
                        .append("/tables/")
                        .append(tableName)
                        .append("/list_keys")
                        .toString();

        log.info(query);
        return query;
    }
}