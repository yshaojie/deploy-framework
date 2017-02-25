#!/bin/sh

#########################################
#### init the server instance evn #######
#########################################

## $1: user $2: ip

function usage(){
  echo "Usage:$0 <usr_name>"
}

function add_user()
{
    name=$1
    if [ "$name" == "" ]
    then
      usage
      exit
    fi
    home=/opt/$name

    echo "Create new user $name,home:$home"
    adduser -m -d "$home" $name
    ln -s /opt/$name /home/$name

    #init the .bash_profile

    echo "init the .bash_profile"
    echo "export LANG=en_US.UTF-8" >> /home/${name}/.bash_profile
    chown "${name}.${name}" /home/$name
}

function init_dir(){
    cd /home/$1/
    mkdir app web log run agent
    mkdir -p app/bak
    mkdir -p web/bak
    cd -
    echo "init dir finished"
}

function init_agent(){
    cd /home/$1/agent/
    rm -rf agent_current.zip
    wget http://10.10.76.79:37449/publish/agent_current.zip
    wget http://10.10.76.79:37449/publish/jdk-7u17-linux-x64.gz
    unzip agent_current.zip
    rm -rf agent_current.zip

    #install jdk7
    tar zxvf jdk-7u17-linux-x64.gz
    rm -rf jdk-7u17-linux-x64.gz
    ln -s jdk1.7.0_17 jdk7
    sed -i "/__server_ip/{s/__server_ip/${2}/;:skip n;b skip;}" bin/agent.sh
    sed -i "/__server_ip/{s/__server_ip/${2}/;:skip n;b skip;}" deploy_server.sh
    echo "init agent finished"
    cd -
}

#########################  get start ###########################

base=`dirname $0`
u_root=`whoami`
user=$1

if [ ${u_root} != "root" ] ;then
    echo "u must be [root] user"
    exit 1
fi

if id -u ${user} >/dev/null 2>/dev/null; then
        echo "user ${user} exists"
        exit 1
else
        echo "user ${user} does not exist, u can install user[${user}] now"
fi

#add user
add_user ${user}

#init the smc user directory
echo "init the smc ${user} directory"
init_dir ${user}

#init agent
init_agent ${user} $2

chown -R "${user}.${user}" /opt/${user}/
cd /home/${user}/agent/bin
echo "init the server instance Finished."
