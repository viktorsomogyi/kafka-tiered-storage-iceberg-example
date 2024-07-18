###############################################################################
#  Licensed to the Apache Software Foundation (ASF) under one
#  or more contributor license agreements.  See the NOTICE file
#  distributed with this work for additional information
#  regarding copyright ownership.  The ASF licenses this file
#  to you under the Apache License, Version 2.0 (the
#  "License"); you may not use this file except in compliance
#  with the License.  You may obtain a copy of the License at
#
#      http://www.apache.org/licenses/LICENSE-2.0
#
#  Unless required by applicable law or agreed to in writing, software
#  distributed under the License is distributed on an "AS IS" BASIS,
#  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
#  See the License for the specific language governing permissions and
# limitations under the License.
###############################################################################


###############################################################################
#SQL CLI - inspired by https://github.com/wuchong/flink-sql-demo/tree/v1.11-EN/sql-client
###############################################################################

FROM flink:1.17.1-scala_2.12-java11

# Create CLI lib folder
COPY docker/sql-client/bin/* /opt/sql-client/
RUN mkdir -p /opt/sql-client/lib

# Download connector libraries
RUN wget -P /opt/sql-client/lib/ https://repo.maven.apache.org/maven2/org/apache/flink/flink-sql-connector-elasticsearch7/3.0.1-1.17/flink-sql-connector-elasticsearch7-3.0.1-1.17.jar
RUN wget -P /opt/sql-client/lib/ https://repo.maven.apache.org/maven2/org/apache/flink/flink-sql-connector-kafka/1.17.1/flink-sql-connector-kafka-1.17.1.jar
RUN wget -P /opt/sql-client/lib/ https://repo.maven.apache.org/maven2/org/apache/flink/flink-connector-jdbc/3.1.1-1.17/flink-connector-jdbc-3.1.1-1.17.jar
RUN wget -P /opt/sql-client/lib/ https://repo.maven.apache.org/maven2/org/apache/flink/flink-json/1.17.1/flink-json-1.17.1.jar
RUN wget -P /opt/sql-client/lib/ https://jdbc.postgresql.org/download/postgresql-42.5.4.jar
RUN wget -P /opt/sql-client/lib/ https://repo.maven.apache.org/maven2/org/apache/flink/flink-sql-avro-confluent-registry/1.17.1/flink-sql-avro-confluent-registry-1.17.1.jar
RUN wget -P /opt/sql-client/lib/ https://github.com/knaufk/flink-faker/releases/download/v0.5.3/flink-faker-0.5.3.jar
RUN wget -P /opt/flink/lib/ https://repo.maven.apache.org/maven2/org/apache/iceberg/iceberg-flink-runtime-1.17/1.5.2/iceberg-flink-runtime-1.17-1.5.2.jar
RUN wget -P /opt/flink/lib/ https://repo1.maven.org/maven2/org/apache/flink/flink-sql-connector-hive-2.3.9_2.12/1.17.1/flink-sql-connector-hive-2.3.9_2.12-1.17.1.jar
RUN wget -P /opt/flink/lib/ https://repo1.maven.org/maven2/org/datanucleus/datanucleus-core/6.0.7/datanucleus-core-6.0.7.jar
RUN wget -P /opt/flink/lib/ https://repo1.maven.org/maven2/org/datanucleus/datanucleus-api-jdo/6.0.2/datanucleus-api-jdo-6.0.2.jar
RUN wget -P /opt/flink/lib/ https://repo1.maven.org/maven2/org/datanucleus/datanucleus-jdo-query/6.0.1/datanucleus-jdo-query-6.0.1.jar
RUN wget -P /opt/flink/lib/ https://repo1.maven.org/maven2/javax/jdo/jdo-api/3.2.1/jdo-api-3.2.1.jar
RUN wget -P /opt/flink/lib/ https://repo1.maven.org/maven2/org/datanucleus/datanucleus-rdbms/6.0.7/datanucleus-rdbms-6.0.7.jar
RUN wget -P /opt/flink/lib/ https://repo1.maven.org/maven2/org/apache/derby/derby/10.17.1.0/derby-10.17.1.0.jar
RUN wget -P /top/flink/lib/ https://repo1.maven.org/maven2/org/apache/derby/derbytools/10.17.1.0/derbytools-10.17.1.0.jar
RUN wget -P /opt/flink/lib/ https://repo1.maven.org/maven2/org/apache/derby/derbyclient/10.17.1.0/derbyclient-10.17.1.0.jar

# Copy configuration
COPY docker/sql-client/conf/* /opt/flink/conf/

COPY docker/sql-client/hadoop-classpath/ /opt/flink/hadoop-classpath/
ENV HADOOP_CLASSPATH /opt/flink/hadoop-classpath/*

ENV CLASSPATH /opt/flink/lib/*

WORKDIR /opt/sql-client
ENV SQL_CLIENT_HOME /opt/sql-client

COPY docker/sql-client/docker-entrypoint.sh /
ENTRYPOINT ["/docker-entrypoint.sh"]