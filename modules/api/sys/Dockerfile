FROM openjdk:11-jre-slim as builder
RUN apt-get update && \
    apt-get install -y --no-install-recommends apt-transport-https apt-utils bc dirmngr gnupg && \
    echo "deb https://repo.scala-sbt.org/scalasbt/debian /" | tee -a /etc/apt/sources.list.d/sbt.list && \
    apt-key adv --keyserver hkp://keyserver.ubuntu.com:80 --recv 2EE0EA64E40A89B84B2DF73499E82A75642AC823 && \
    apt-get update && \
    apt-get upgrade -y && \
    apt-get install -y --no-install-recommends sbt && \
    apt-get install -y wget &&  \
    apt-get install -y zip

RUN wget https://www.yourkit.com/download/docker/YourKit-JavaProfiler-2022.3-docker.zip -P /tmp/ && \
  unzip /tmp/YourKit-JavaProfiler-2022.3-docker.zip -d /usr/local && \
  rm /tmp/YourKit-JavaProfiler-2022.3-docker.zip

COPY . ./ergo-analytics
WORKDIR /ergo-analytics
RUN sbt api/assembly
RUN mv `find . -name api-assembly-0.1.0.jar` /api.jar
CMD ["/usr/bin/java", "-jar", "/api.jar"]

FROM openjdk:11-jre-slim

ENV LOG_CONFIG "conf/logback-prod.xml"
ENV ENV_CONFIG "conf/api.conf"

COPY --from=builder /api.jar /api.jar
COPY --from=builder /usr/local/YourKit-JavaProfiler-2022.3 /usr/local/YourKit-JavaProfiler-2022.3
ENTRYPOINT java-agentpath:/usr/local/YourKit-JavaProfiler-2022.3/bin/linux-x86-64/libyjpagent.so=port=10001,listen=all  -Xms1G -Xmx2G -Dlogback.configurationFile=${LOG_CONFIG} -jar /api.jar ${ENV_CONFIG}
