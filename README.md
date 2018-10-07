### Flink Demo
Basic Flink jobs to play around with

```
sbt clean assembly
flink run -c com.trystanj.KafkaWordCount ./target/scala-2.11/flink-demo-assembly-0.1-SNAPSHOT.jar localhost:9092 test
```

For Kafka, create a `test` topic first, and push strings to it:

`bin/kafka-console-producer.sh --broker-list localhost:9092 --topic test`

Then, assuming Flink was installed with Homebrew:
```bash
brew info apache-flink
-> /usr/local/Cellar/apache-flink/1.6.1
```

```bash
tail -f /usr/local/Cellar/apache-flink/1.6.1/libexec/log/flink-*-taskexecutor-*.out
```

```
WordWithCount(hey (kafka),3)
WordWithCount(neat (kafka),2)
WordWithCount(cool (kafka),1)
WordWithCount(wow (kafka),1)
```

### To Do
1. Add sinks
    1. elasticsearch
    1. redis
1. Handle running aggregations and periodic updates?
1. Add Async I/O to make http request on msg received
1. Merge two kafka streams
