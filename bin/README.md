# Sensor data rest #


RIAK TS has own REST HTTP API that can be used.

* add sensor data to DB
* read sensor data on some conditions from DB
* delete old sensor data ?

## Prerequisities

* Docker
* Docker compose


## Riak TS installation


Run

```
docker-compose up
```

or

```
docker-compose up -d coordinator
```

* to install Riak TS more info https://hub.docker.com/r/basho/riak-ts/
* After installation go to **Riak Explorer** http://localhost:8098/admin/#/cluster/default/


## Scaling

Scaling is achieved via docker-compose command for now. Example for 4 RIAK TS instances

```
$ docker-compose scale member=4
```

## RIAK REST API

* RIAK TS has own REST HTTP API that can be used.
* by default is accessible on port 8098



## TODO

* install RIAK TS on server in Kubernetes
* algorithm to reduce data on server side for fast rendering on client side


## How to work with this rest API


## Endpoints

* http://[addr]:8098/ts/v1/tables/SensorData/keys ‘[»Row(s)«]’ POST ( put )
* http://[addr]:8098/ts/v1/tables/SensorData/keys/deviceId/bar/time/1506339989/type/mi DELETE ( delete )
* http://[addr]:8098/ts/v1/tables/SensorData/list_keys GET ( list_keys )
* http://[addr]:8098/ts/v1/tables/SensorData/keys/deviceId/bar/time/1506339989/type/mi GET ( get )
* http://[addr]:8098/ts/v1/query –data “»Query«” POST ( query )




### Get all keys from table Sensordata. Expensive REST call. Be aware :)
```
$ curl -XGET http://127.0.0.1:8098/ts/v1/tables/SenzorData/list_keys
```
Response
```
http://172.19.0.2:8098/ts/v1/tables/SensorData/keys/deviceId/bar/time/1506339989/type/mi
http://172.19.0.2:8098/ts/v1/tables/SensorData/keys/deviceId/foo/time/1506340107/type/bar
http://172.19.0.2:8098/ts/v1/tables/SensorData/keys/deviceId/foo/time/1506340047/type/sit
http://172.19.0.2:8098/ts/v1/tables/SensorData/keys/deviceId/foo/time/1506339988/type/mi
```

### Get all data by specific query
```
curl -XPOST http://127.0.0.1:8098/ts/v1/query --data "SELECT * from SensorData WHERE deviceId = 'foo' AND time > 1506253923 AND time < 1506426723"
```
Response
```
{"columns":["deviceId","type","value","time"],"rows":[["foo","mi","sed",1506339988],["foo","sit","amet",1506340047],["foo","bar","Lorem",1506340107]]}
```
Some rules

* time have to go last
* time have to has since - from boundaries
* where clause have to has all atributes already present in partition key (time,deviceId)

### Delete one record

```
curl -XDELETE http://127.0.0.1:8098/ts/v1/tables/SensorData/keys/deviceId/foo/type/mi/time/1506339988
```
Response
```
{"success":true}
```
Some rules

* for deleting you need at least 3 atributes / columns. (maybe it is local key part of primary key)
* question is how much columns for deleting we need with 2 columns at all in table ? :D TODO test it


### Get one record

* see get https://www.tiot.jp/riak-docs/riak/ts/1.3.0/developing/http/

### insert one or more records

* see put https://www.tiot.jp/riak-docs/riak/ts/1.3.0/developing/http/

### DATA Modeling

* Quantums default number is 5000. Can be configured to higher number. But be aware of overload. High number means more data -> Here comes into play our reduce rest service for reading.
* Quantum size (in sec/min/days) how much data to one partition regarding to timestamp. (Performance, required so cannot be )
* VNodes from 64 to 1024. Maximum is 1024. (limitated)
* Each node in a Riak TS cluster manages one or many virtual nodes, or vnodes.
* VNode = partition. (really ?)

## More informations and wiki

* [Data modeling](https://github.com/cvitter/Riak-TS-Data-Modeling/blob/master/How%20Partition%20Keys%20Work.md)
* [How to select right partition key](https://github.com/cvitter/Riak-TS-Data-Modeling/blob/master/Selecting%20A%20Partition%20Key.md)
* [Official site](http://basho.com/products/riak-ts/)


### Other useful projects

* https://github.com/basho-labs
* https://github.com/basho-labs/riak_explorer


### Alternatives

* **Dalmatimer DB** : If the speed is the issue : https://blog.outlyer.com/time-series-database-benchmarks
* https://docs.google.com/spreadsheets/d/1sMQe9oOKhMhIVw9WmuCEWdPtAoccJ4a-IuZv4fXDHxM/edit#gid=0
* is much much faster in comparison with RIAK TS but the scala API has bug and Erlang official API is too far from Java and documentation is very poor