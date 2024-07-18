FROM flink:1.16

ENV APACHE_HADOOP_URL=https://archive.apache.org/dist/hadoop/
ENV HADOOP_VERSION=2.8.5
ENV SCALA_VERSION=2.12
ENV FLINK_VERSION=1.16.2
ENV HIVE_VERSION=2.3.9
ENV MAVEN_URL=https://repo1.maven.org/maven2
ENV FLINK_CONNECTOR_URL=${MAVEN_URL}/org/apache/flink
ENV FLINK_CONNECTOR_PACKAGE=flink-sql-connector-hive

WORKDIR /tmp
# install Kafka connector
RUN wget -P /opt/flink/lib https://repo.maven.apache.org/maven2/org/apache/flink/flink-sql-connector-kafka_${SCALA_VERSION}/1.14.6/flink-sql-connector-kafka_${SCALA_VERSION}-1.14.6.jar

# install Iceberg connector
RUN wget -P /opt/flink/lib https://repo.maven.apache.org/maven2/org/apache/iceberg/iceberg-flink-runtime-1.16/1.5.2/iceberg-flink-runtime-1.16-1.5.2.jar

#RUN wget -P /opt/flink/lib ${FLINK_CONNECTOR_URL}/${FLINK_CONNECTOR_PACKAGE}-${HIVE_VERSION}_${SCALA_VERSION}/${FLINK_VERSION}/${FLINK_CONNECTOR_PACKAGE}-${HIVE_VERSION}_${SCALA_VERSION}-${FLINK_VERSION}.jar

#RUN wget ${APACHE_HADOOP_URL}/common/hadoop-${HADOOP_VERSION}/hadoop-${HADOOP_VERSION}.tar.gz
#RUN tar xzf hadoop-${HADOOP_VERSION}.tar.gz

#ENV HADOOP_HOME=/tmp/hadoop-${HADOOP_VERSION}
#ENV PATH=${HADOOP_HOME}/bin:$PATH

COPY ../sql-client/hadoop-classpath/ /opt/flink/hadoop-classpath/
ENV HADOOP_CLASSPATH=/opt/flink/hadoop-classpath/*

#RUN set -x && \
#    echo "$(hadoop classpath --glob)" >> hadoop_classpath && \
#    tr ':' '\n' < hadoop_classpath > hadoop_classpath_formatted && \
#    echo hadoop_classpath_formatted && \
#    while IFS="" read -r p || [ -n "$p" ]; do [ -f "$p" ] && printf '%s\n' "$p"; done < hadoop_classpath_formatted

WORKDIR /opt/flink

#RUN hadoop classpath --glob > /tmp/hadoop_classpath
#
#RUN find /app/jars -name "*.jar" -exec cp {} /app/libs/ \;
#RUN echo "export HADOOP_CLASSPATH=$(hadoop classpath --glob)" >> ~/.bash_profile
#RUN echo "export CLASSPATH=$(hadoop classpath --glob)" >> ~/.bash_profile
#
#SHELL ["/bin/bash", "-lc"]