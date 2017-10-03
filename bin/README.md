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