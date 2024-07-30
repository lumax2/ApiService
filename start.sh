#!/bin/bash

if [ "$ZH_APP_MEM" == "" ]; then
  vmMemory=" -Xms2G -Xmx2G "
else
  vmMemory=" -Xms$ZH_APP_MEM -Xmx$ZH_APP_MEM "
fi

echo "skywalking_name:$skywalking_name"
echo "skywalking_host:$skywalking_host"

java -javaagent:/app/skywalking-agent.jar -Dskywalking.agent.service_name=$skywalking_name -Dskywalking.collector.backend_service=$skywalking_host \
     -server -jar $vmMemory \
     -Duser.timezone=GMT+08 \
     /app/app.jar \
     --spring.cloud.consul.discovery.hostname=$SERVICE_HOST \
     --spring.cloud.consul.discovery.port=$SERVICE_PORT \
     --spring.cloud.consul.discovery.healthCheckUrl=http://$SERVICE_HOST:$SERVICE_PORT/base/health \
     --spring.profiles.active=\${env} \
     --xxl.job.executor.register.ip=$SERVICE_HOST \
     --xxl.job.executor.register.port=\${xxl-job-port}
