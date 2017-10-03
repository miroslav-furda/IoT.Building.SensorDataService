# Darwin sensor data rest microservice #

Features

- delegating rest calls to RIAK TS REST API on 8098 default port
- for query data reducing algorithm is used on top of RIAK TS REST API call


## Technologies

* Spring Boot
* Docker compose
* RIAK TS


## Consumed endpoints

* http://[addr]:8098/ts/v1/query –data “»Query«” POST ( query )
* http://[addr]:8098/ts/v1/tables/{table}/keys/deviceId/{deviceId}/type/{type}/time/{time} DELETE ( delete )
* http://[addr]:8098/ts/v1/tables/{table}/keys/deviceId/{deviceId}/type/{type}/time/{time} GET ( get one )
*


## Reducing algorithm

* ued for endpoint http://[addr]:8098/ts/v1/query –data “»Query«” POST ( query )
* inputs : from time , to time, constant graph point size
* how much records -> [ reducing ] -> constant graph point size

## Data


* Service return data description as "columns" and data as "rows".
* Each row is Object.

```json
{
  "columns": [
    "deviceId",
    "type",
    "value",
    "time"
  ],
  "rows": [
    {
      "deviceId": "222",
      "type": "humidity",
      "time": 1506676358,
      "value": "12.2"
    },
    {
      "deviceId": "222",
      "type": "humidity",
      "time": 1506676999,
      "value": "12.3"
    },
    {
      "deviceId": "111",
      "type": "temperature",
      "time": 1506677045,
      "value": "24.3"
    }
  ]
}
```

## Configurations

* See application.properties file.
* windows size
* microservices runtime port
* data endpoint url from where the data are consumed


## How to run microservice

TODO

## Query 1 or more data

```
curl -XPOST http://127.0.0.1:7777/ts/v1/query --data "SELECT * from SensorData WHERE deviceId = 'foo' AND time > 1506253923 AND time < 1506426723"
```

Response

```json
{
  "columns": [
    "deviceId",
    "type",
    "value",
    "time"
  ],
  "rows": [
    {
      "deviceId": "222",
      "type": "humidity",
      "time": 1506676358,
      "value": "12.2"
    },
    {
      "deviceId": "222",
      "type": "humidity",
      "time": 1506676999,
      "value": "12.3"
    },
    {
      "deviceId": "111",
      "type": "temperature",
      "time": 1506677045,
      "value": "24.3"
    }
  ]
}
```

## Delete one row

```
curl -XDELETE "http://localhost:7777/ts/v1/tables/SensorData/keys/deviceId/foo/type/sit/time/1506340047"
```

Response

{"success":true}


## Get one object

```
curl -XGET http://localhost:7777/ts/v1/tables/SensorData/keys/deviceId/foo/type/bar/time/1506340107
```

Response

```
{
  "deviceId": "foo",
  "type": "bar",
  "time": 1506340107,
  "value": "Lorem"
}
```