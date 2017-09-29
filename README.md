# Darwin sensor data rest microservice #

Sometimes the other microservices and clients need to read less data than present in query.
This microservice read senzor data by other data microservice see sensor-data-rest and reduce the
query endpoint to be possible to consume and render them.


## Technologies

* Spring Boot
* Consuming existing microservice senzor-data-rest.
* Consumed endpoint http://[addr]:8098/ts/v1/query –data “»Query«” POST ( query )


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

## Examples

```
curl -XPOST http://127.0.0.1:7777/ts/v1/query --data "SELECT * from SensorData WHERE deviceId = 'foo' AND time > 1506253923 AND time < 1506426723"
```