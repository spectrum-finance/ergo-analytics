FROM ubuntu:20.04

RUN apt-get update && apt-get install -y openjdk-11-jdk

RUN  apt-get update && apt-get install -y wget && apt-get install -y zip

RUN wget https://www.yourkit.com/download/docker/YourKit-JavaProfiler-2022.3-docker.zip -P /tmp/ && \
  unzip /tmp/YourKit-JavaProfiler-2022.3-docker.zip -d /usr/local && \
  rm /tmp/YourKit-JavaProfiler-2022.3-docker.zip

ENV LOG_CONFIG "conf/logback-prod.xml"
ENV ENV_CONFIG "conf/mempool.conf"

ENV RUN_JAR "/usr/mempool-0.1.0.jar"

ENTRYPOINT java -agentpath:/usr/local/YourKit-JavaProfiler-2022.3/bin/linux-x86-64/libyjpagent.so=port=10001,listen=all  -Xms2G -Xmx4G -Dlogback.configurationFile=${LOG_CONFIG} -jar ${RUN_JAR} ${ENV_CONFIG}
