git pull
sbt mempool/assembly
# mempool-assembly-0.1.0.jar
docker build -t mempool-profiler:0.0.1 -f modules/mempool/sys/Dockerfile .