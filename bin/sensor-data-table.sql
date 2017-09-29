-- partition key : must be in query where clause
-- local key :

-- Primary key – defines data location and is composed of the partition key and the local key.
--   - Partition key – defines where data is placed on the cluster, i.e which node the data will be written to
--   - Local key – defines where data is written in the partition on that given node.


CREATE TABLE ExampleTable
(
  deviceId       varchar   not null,
  type           varchar   not null,
  value 		 varchar   not null,
  time           timestamp not null,
  PRIMARY KEY (
    (deviceId, quantum(time, 12, 'h')),
    deviceId, time, type
  )
)