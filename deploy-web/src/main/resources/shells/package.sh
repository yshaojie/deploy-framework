#!/bin/bash
start_time=`date +%s`
########################parm############################
package_name="${module}-dist.zip"
package_dir="${project}/${module}/target"
git_branch=${branch}
env_package_cmd="mvn  clean package -U -Dmaven.test.skip=true -Dmaven.compile.fork=true -pl ${module} -am "
echo "${env_package_cmd}"
#base="/tmp"
base="/data/deploy"
build=${base}/projects

link_path="${base}/dist-source/"

########################################################

#develop env
rm -rf ${build}/${project}
cd ${build}
git clone ${project_git_path} -b ${git_branch}
cd ${build}/${project}
#mvn execute
${env_package_cmd}
cd ${module}
if [ ! -f target/${module}-*-dist.zip ];  then
    echo "error:no such file target/${module}-*-dist.zip package fail"
    exit 1
fi

cp target/${module}-*-dist.zip ${link_path}/${module}.zip

echo "package finish."
end_time=`date +%s`
echo "exec time $((end_time-start_time)) s"
exit 0

