package com.trystanj

/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.util.Properties

import org.apache.flink.api.common.serialization.SimpleStringSchema
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer011

object KafkaWordCount {

  def main(args: Array[String]) {
    if (args.length != 2) {
      System.err.println("USAGE:\nKafkaWordCount <hostname:port> <topic>")
      return
    }

    val hostName = args(0)
    val topic = args(1) // also used for group.id

    val properties = new Properties()
    properties.setProperty("bootstrap.servers", hostName)
    properties.setProperty("group.id", topic)

    val env = StreamExecutionEnvironment.getExecutionEnvironment
    val consumer = new FlinkKafkaConsumer011[String](topic, new SimpleStringSchema(), properties)
    val stream = env.addSource(consumer)

    val counts = stream
      .flatMap { _.toLowerCase.split("\\W+") filter { _.nonEmpty } }
      .map(w => WordWithCount(w + " (kafka)", 1))
      .keyBy("word")
      .timeWindow(Time.seconds(5), Time.seconds(5))
      .sum(1)

    counts.print()

    env.execute("Scala SocketTextStreamWordCount Example")
  }

  // Data type for words with count
  case class WordWithCount(word: String, count: Long)
}
