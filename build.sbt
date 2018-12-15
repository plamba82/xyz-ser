

name := "akka_stream_ime"

version := "1.0"

scalaVersion := "2.12.4"



val akkaVersion = "2.5.8"
val akkaHttpVersion = "10.0.11"
val elasticVersion = "5.6.5"

val lettuceVersion = "4.4.2.Final"

val protoBufVersion = "3.5.0"

val log4jVersion = "2.10.0"

val logbackVersion = "1.2.3"
val guiceVersion = "4.1.0"


libraryDependencies ++= Seq(

  "com.typesafe.akka" %% "akka-actor" % akkaVersion,
  "com.typesafe.akka" %% "akka-stream" % akkaVersion,
  "com.typesafe.akka" %% "akka-http" % akkaHttpVersion,
  "com.typesafe.akka" %% "akka-http-core" % akkaHttpVersion,
  "com.typesafe.akka" %% "akka-remote" % akkaVersion,
  "com.typesafe.akka" %% "akka-contrib" % akkaVersion,
  "com.typesafe.akka" %% "akka-cluster-tools" % akkaVersion,
  "com.typesafe.akka" %% "akka-protobuf" % akkaVersion,
  "com.typesafe.akka" %% "akka-cluster" % akkaVersion,
  "com.typesafe.akka" %% "akka-cluster-sharding" %  akkaVersion,
  "com.typesafe.akka" %% "akka-distributed-data" % akkaVersion,
  "com.typesafe.akka" %% "akka-persistence" % akkaVersion,
  "com.typesafe.akka" %% "akka-slf4j" % akkaVersion,
  "com.typesafe.akka" %% "akka-cluster-metrics" % akkaVersion,
  "com.fasterxml.jackson.core" % "jackson-core" % "2.8.5",
  "com.fasterxml.jackson.core" % "jackson-databind" % "2.8.5",
  "com.fasterxml.jackson.core" % "jackson-annotations" % "2.8.5",
  "com.cwbase" % "logback-redis-appender" % "1.1.5",
  "org.slf4j" % "slf4j-api" % "1.7.25",
  "org.apache.logging.log4j" % "log4j-core" % log4jVersion,
  "org.apache.logging.log4j" % "log4j-api" % log4jVersion,
  "org.apache.logging.log4j" % "log4j-to-slf4j" % log4jVersion,
  "net.logstash.logback" % "logstash-logback-encoder" % "4.11",
  "ch.qos.logback" % "logback-core" % logbackVersion,
  "ch.qos.logback" % "logback-classic" % logbackVersion,
  "org.asynchttpclient" % "async-http-client" % "2.0.38",

  "org.elasticsearch" % "elasticsearch" % elasticVersion,
  "org.elasticsearch.client" % "transport" % elasticVersion,

  "com.google.code.gson" % "gson" % "2.8.0",
  "org.apache.commons" % "commons-pool2" % "2.4.2",
  "org.apache.axis" % "axis" % "1.4",
  "javax.xml" % "jaxrpc-api" % "1.1",
  "wsdl4j" % "wsdl4j" % "1.6.2",
  "org.deeplearning4j" % "deeplearning4j-core" % "1.0.0-beta2",
  "org.nd4j" % "nd4j-native-platform" % "1.0.0-beta2",
  "com.twelvemonkeys.imageio" % "imageio-core" % "3.1.2",
  "com.twelvemonkeys.common" % "common-lang" % "3.1.2"
).map(_.exclude("com.google.code.maven-play-plugin.org.allcolor.yahp", "*"))
sources in (Compile, doc) := Seq.empty

publishArtifact in (Compile, packageDoc) := false


fork in run := true
