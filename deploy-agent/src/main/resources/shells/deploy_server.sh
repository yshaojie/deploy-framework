#!/bin/bash
#server_name server_args server_jvm_args server_main_class
server_ip="{server_ip}"
server_name="${server_name}"
server_home="/servers/${server_name}"
server_log_home="/data/logs/${server_name}"
server_args="${main_args}"
server_jvm_args="${jvm_args}"
server_main_class="${main_class}"
server_resources="${server_home}/resources/"
packge_zip="${server_name}.zip"

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

cd ${server_home}

##command is success function
function command_is_success(){
    if [ ! $? -eq 0 ]; then
        echo $1
        exit 1
    fi
}

get_server_pids() {
    pids=( `ps -eo pid,user,cmd | grep "python -m SimpleHTTPServer" | grep 8000 | awk '$3=="python" {print $1}' ` )
}

set_high_priority_jar() {
    #find the jars
    jars=( `find ${server_home}/lib/ -name $1* | sort -fr` )
    if [ "${#jars[@]}" -gt 0 ]; then
        #exist logback-classic jar
        echo "find ${jars[0]}, set it to the java classpath"
        server_class_path=$server_class_path:${jars[0]}
    fi
}

start_server() {
    echo "starting server ${server_name}"
    #all server pids
    get_server_pids
    if [ ${#pids[*]} -gt 0 ]; then
        echo "${server_name} is started,the pid is ${pids[@]}"
        exit 1
    fi

    echo ""
    echo "#########server configs################################################################################"
    echo "  server_ip=${server_ip}"
    echo "  server_args=${server_args}"
    echo "  server_jvm_args=${server_jvm_args}"
    echo "  server_name=${server_name}"
    echo "  server_main_class=${server_main_class}"
    echo "  server_home=${server_home}"
    echo "  server_log_home=${server_log_home}"
    echo "  server_resources=${server_resources}"
    echo "#########server configs################################################################################"
    echo ""

    #set logback-classic jar
    set_high_priority_jar logback-classic

    #log4j-over-slf4j jar
    set_high_priority_jar log4j-over-slf4j

    #jul-to-slf4j jar
    set_high_priority_jar jul-to-slf4j

    #log4j-over-slf4j jar
    set_high_priority_jar jcl-over-slf4j


    #iterator server jar libs
    for i in `ls ${server_home}/lib/ | sort -rf`
    do
        要换方式
        for f in /home/shaojieyue/tools/workspace/*; do
          echo "File -> $f"
        done
        server_class_path=$server_class_path:${server_home}/lib/$i
    done
    export CLASSPATH=${server_class_path}
    #server start time
    start_time=`date "+%Y-%m-%d %H:%M:%S"`

    #start the server
    java ${server_jvm_args} -Dserver_ip=${server_ip} -Dstart_time="${start_time}" -Dserver_home=${server_home} -Dserver_log_home=${server_log_home} -Dserver_resources=${server_resources} -Dserver_name=${server_name} ${system_props} ${server_main_class} ${server_args} >> ${server_log_home}/${server_name}.log 2>&1 &
    sleep 3
    get_server_pids
    if [ ${#pids[*]} -gt 1 ]; then
        echo "${server_name} is start success,but find multiple instances pids: ${pids[@]}"
        exit 1
    elif [ ${#pids[*]} -eq 1 ]; then
        echo "${server_name} is start success,the pid is ${pids[@]}"
    else
        echo "Fail,see log ${server_log_home}/${server_name}.log"
        exit 1
    fi
}

stop_server() {
    echo "stoping server ${server_name}"
    #all pids
    get_server_pids
    if [ ${#pids[*]} -gt 1 ]; then #multiple server
        echo "find multiple ${server_name} proccess, infos:"
        ps -eo pid,user,cmd | grep "${server_resources}" | grep "${server_main_class}"
        echo "stop server fail,please manual processing"
        exit 1
    elif [ ${#pids[*]} -eq 1 ]; then
        echo "kill ${pids[@]}"
        kill ${pids[@]}
        for (( i = 0; i < 5; i ++ ))
        do
            sleep 1
            get_server_pids
            if [ ${#pids[*]} -le 0 ]; then
                break
            fi
        done
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
        rm -f ${server_home}/${packge_zip}
        echo "wget -cq ${source_path}"
        wget -cqO ${server_home}/${packge_zip} ${source_path}
        command_is_success "wget file ${source_path} fail."
        stop_server
        #clear dir
        rm -rf ${server_home}/lib ${server_home}/resources
        unzip -oq ${packge_zip} -d ${server_home}
        start_server
    ;;
    delete)
        echo "delete server ${server_name}"
        stop_server
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
        start_server
    ;;
    stop)
        stop_server
    ;;
    restart)
        stop_server
        start_server
    ;;
    *)
        echo "Usage ${0} <start|stop|restart>"
        exit 1
    ;;
esac
exit 0