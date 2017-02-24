#!/bin/sh

server_home="${HOME}/app/${server_name}"
server_log_home="${HOME}/log/${server_name}"
cd ${HOME}/app/
if [ -d "${server_home}" ]; then
    ${serverName}/server.sh stop
    echo "delete server ${server_name}"
    #del server
    rm -rf ${server_home}
    #del log dir
    rm -rf ${server_log_home}
    echo "delete the "${server_name}" succesfull"
    exit 0
fi
exit 0