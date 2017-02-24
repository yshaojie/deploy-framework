#!/bin/sh
#server_name server_args server_jvm_args server_main_class
server_ip="__server_ip"
server_name="${server_name}"
server_home="${HOME}/app/${server_name}"
server_log_home="${HOME}/log/${server_name}"
server_args="${main_args}"
server_jvm_args="${jvm_args}"
server_main_class="${main_class}"
server_resources="${server_home}/resources/"

echo "##################################################################"
echo "server_ip=${server_ip}"
echo "server_name=${server_name}"
echo "server_home=${server_home}"
echo "server_log_home=${server_log_home}"
echo "server_args=${server_args}"
echo "server_jvm_args=${server_jvm_args}"
echo "server_main_class=${server_main_class}"
echo "server_resources=${server_resources}"
echo "####################################################################"

if [ ! -d ${server_home} ]; then
    echo "server not exist"
    exit 0
fi

if [ -f "/usr/bin/java" ]; then
    JAVA_HOME=/usr/java/default
    PATH=$JAVA_HOME/bin:$PATH
    CLASSPATH=.:$JAVA_HOME/lib:$JAVA_HOME/lib/tools.jar:$JAVA_HOME/lib/dt.jar
    export JAVA_HOME PATH CLASSPATH
fi

cd ${server_home}

##command is success function
function command_is_success(){
    if [ ! $? -eq 0 ]; then
        echo $1
        exit 1
    fi
}

function get_server_pids() {
    pids=( `ps -eo pid,user,cmd | grep "${server_resources}" | grep "${server_main_class}" | awk '{print $1}'` )
}

##start server
function start(){
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
    java  ${server_jvm_args} -Dstart_time="${start_time}" -Dhost_home=${HOME} -Dserver_log_home=${server_log_home} -Xloggc:${server_log_home}/gc.log -Dserver_ip=${server_ip} -Dserver_resources=${server_resources} -Dserver_name=${server_name} ${system_props} ${server_main_class} ${server_args} >> ${server_log_home}/${server_name}.log  2>>${server_log_home}/${server_name}.err &
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
}

##stop server
function stop(){
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
        sleep 3
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
}

case "$1" in
    deploy)
        rm -f ${server_home}/${source_path}
        echo "wget -cq http://10.10.76.79:37449/publish/${source_path}"
        wget -cqO ${server_home}/${source_path} http://10.10.76.79:37449/publish/${source_path}
        command_is_success "wget file ${source_path} fail."
        stop
        #clear dir
        rm -rf ${server_home}/lib ${server_home}/resources
        unzip -oq ${source_path} -d ${server_home}
        start
    ;;
    delete_server)
        echo "delete server ${server_name}"
        stop
        sleep 2
        #del server
        echo "delete dir: ${server_home}"
        rm -rf ${server_home}
        #del log dir
        echo "delete dir: ${server_log_home}"
        rm -rf ${server_log_home}
        echo "delete the server "${server_name}" succesfull"
    ;;
    start)
        start
    ;;
    stop)
        stop
    ;;
    restart)
        stop
        start
    ;;
    *)
        echo "Usage ${0} <start|stop|restart>"
        exit 1
    ;;
esac

exit 0