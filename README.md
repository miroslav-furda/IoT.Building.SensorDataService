# Darwin sensor data rest microservice #

Features

* delegating rest calls to RIAK TS REST API on 8098 default port
* for query data reducing algorithm is used on top of RIAK TS REST API call
* this service is listening on port 7777, RIAK TS on port 8098
* delegated ( list_keys, get, put, delete )
* not just delegated also reduced ( query )



## Technologies

* Spring Boot
* Docker compose
* RIAK TS (see documentation for more information [here](https://bitbucket.org/iotresearchlab/darwin-sensor-data-rest/src/0ad28277d2d6b96ee5971f3d09096c4132c4a3a9/bin/README.md?at=master&fileviewer=file-view-default))


## Consumed endpoints

* http://[addr]:7777/ts/v1/query –data “»Query«” POST ( query )
* http://[addr]:7777/ts/v1/tables/{table}/keys/deviceId/{deviceId}/type/{type}/time/{time} DELETE ( delete )
* http://[addr]:7777/ts/v1/tables/{table}/keys/deviceId/{deviceId}/type/{type}/time/{time} GET ( get one )
* http://[addr]:7777/ts/v1/tables/SensorData/list_keys GET ( list_keys )
* http://[addr]:7777/ts/v1/tables/SensorData/keys ‘[»Row(s)«]’ POST ( put )


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

Some rules

* time have to go last
* time have to has since - from boundaries
* where clause have to has all atributes already present in partition key (time,deviceId)


## Delete one row

```
curl -XDELETE "http://localhost:7777/ts/v1/tables/SensorData/keys/deviceId/foo/type/sit/time/1506340047"
```

Response

{"success":true}

Some rules


* for deleting you need at least 3 atributes / columns. (maybe it is local key part of primary key)
* question is how much columns for deleting we need with 2 columns at all in table ? :D TODO test it

## Get one object

```
curl -XGET http://localhost:7777/ts/v1/tables/SensorData/keys/deviceId/foo/type/bar/time/1506340107
```

Response

```json
{
  "deviceId": "foo",
  "type": "bar",
  "time": 1506340107,
  "value": "Lorem"
}
```

## Get all keys

```
$ curl -XGET http://localhost:7777/ts/v1/tables/SensorData/list_keys
```

Response

```
http://172.19.0.2:8098/ts/v1/tables/SensorData/keys/deviceId/vehicula/type/mi/time/1505296853/value/sed
http://172.19.0.2:8098/ts/v1/tables/SensorData/keys/deviceId/foo/type/bar/time/1505296973/value/Lorem
http://172.19.0.2:8098/ts/v1/tables/SensorData/keys/deviceId/Weather%20Station%200001/type/abc-xxx-001-001/time/2017091410/value/14.5
http://172.19.0.2:8098/ts/v1/tables/SensorData/keys/deviceId/Aliquam/type/sit/time/1505296913/value/amet
```


## Put one or more data

TODO img




Response

```
{"success":true}
```

Some rules

* Be aware if you try to store same data , you got from server also success message, but nothing will be stored.
* Same data means that the data has same local key. In this case local key = ( deviceId, type, time ).