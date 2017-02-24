#!/bin/bash

# template properites:
# server_name: the instance name of the server process,should be unique in all machines.
# server_class_path: the class path of the server process,may be empty
# server_main_class: the main class of the server process,must be set
# server_args: the arguments of of the server process,may be empty
# server_jvm_args: the jvm parameters of of the server process,may be empty

###########################################
###########set defalut java env############
###########################################
if [ -f "/usr/bin/java" ]; then
    JAVA_HOME=/usr/java/default
    PATH=$JAVA_HOME/bin:$PATH
    CLASSPATH=.:$JAVA_HOME/lib:$JAVA_HOME/lib/tools.jar:$JAVA_HOME/lib/dt.jar
    export JAVA_HOME PATH CLASSPATH
fi
###########################################

###########################################
########replaced parameters################
###########################################
server_args="__server_args"
ip="__ip"
server_jvm_args="__server_jvm_args"
server_name="__server_name"
server_main_class="__server_main_class"
###########################################

server_home="${HOME}/app/${server_name}"
server_log_home="${HOME}/log/${server_name}"
server_gc_log=${server_log_home}/gc.log
server_resources="${server_home}/resources/"

if [ ! -d ${server_log_home} ]
then
    mkdir -p ${server_log_home}
fi

function get_server_pids() {
    pids=( `ps -eo pid,user,cmd | grep "${server_resources}" | grep "${server_main_class}" | awk '{print $1}'` )
}


case "$1" in
    start)
        echo "starting server ${server_name}"
        #all pids
        get_server_pids
        if [ ${#pids[*]} -gt 0 ]; then
            echo "${server_name} is started,the pid is ${pids[@]}"
            exit 1
        fi

        if [ -z "${server_class_path}" ]; then
            for i in `ls ${server_home}/lib/`
            do
                server_class_path=$server_class_path:${server_home}/lib/$i
            done
        fi
        export CLASSPATH=${server_class_path}

        #server start time
        start_time=`date "+%Y-%m-%d %H:%M:%S"`
        java  ${server_jvm_args} -Dstart_time="${start_time}" -Dhost_home=${HOME} -Dserver_log_home=${server_log_home} -Xloggc:${server_gc_log} -Dserver_ip=${ip} -Dserver_resources=${server_resources} -Dserver_name=${server_name} ${system_props} ${server_main_class} ${server_args} >> ${server_log_home}/${server_name}.log  2>>${server_log_home}/${server_name}.err &
        sleep 3
        get_server_pids
        if [ ${#pids[*]} -gt 1 ]; then
            echo "${server_name} is start success,but find multiple instances pids: ${pids[@]}"
            exit 1
        elif [ ${#pids[*]} -eq 1 ]; then
            echo "${server_name} is start success,the pid is ${pids[@]}"
        else
            echo "Fail,see log ${server_log_home}/${server_name}.err"
            exit 1
        fi
    ;;
    stop)
        echo "stoping server ${server_name}"
        #all pids
        get_server_pids
        if [ ${#pids[*]} -gt 1 ]; then
            echo "find multiple ${server_name} proccess, infos:"
            ps -eo pid,user,cmd | grep "${server_resources}" | grep "${server_main_class}"
            echo "stop server fail,please manual processing"
            exit 1
        elif [ ${#pids[*]} -eq 1 ]; then
            echo "kill -9 ${pids[@]}"
            kill -9 ${pids[@]}
            get_server_pids
            if [ ${#pids[*]} -gt 0 ]; then
                echo "stop server fail,please manual processing"
                exit 1
            else
                echo "stop server success"
            fi
        else
            echo "server ${server_name} is not exist"
        fi
    ;;
    restart)
        $0 stop
        if [ ! $? -eq 0 ]; then
            exit 1
        fi
        $0 start
    ;;
    *)
        echo "Usage ${0} <start|stop|restart>"
        exit 1
    ;;
esac
exit 0