package com.self.agent.service;

import com.self.deploy.common.Command;
import com.self.deploy.common.bean.Result;
import com.self.deploy.common.bean.ServerInstanceConfig;
import com.self.deploy.common.util.ShellUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by shaojieyue
 * Created time 2017-02-25 13:56
 */

@Service
public class CommandService {
    private static final Logger logger = LoggerFactory.getLogger(CommandService.class);
    public static final String SHELL_PATH = System.getProperty("resources","/servers/agent/resources/")+"/shells/";
    public static final String INIT_SERVER_SHELL = "/init_server.sh";
    public static final String DEPLOY_SERVER_SHELL = "/deploy_server.sh";

    public Result initServer(ServerInstanceConfig serverInstanceConfig) {
        return execCommand(Command.INIT_SERVER,serverInstanceConfig);
    }

    public Result deployServer(ServerInstanceConfig serverInstanceConfig) {
        return execCommand(Command.DEPLOY,serverInstanceConfig);
    }

    public Result startServer(ServerInstanceConfig serverInstanceConfig) {
        return execCommand(Command.START,serverInstanceConfig);
    }

    public Result restartServer(ServerInstanceConfig serverInstanceConfig) {
        return execCommand(Command.RESTART,serverInstanceConfig);
    }

    public Result stopServer(ServerInstanceConfig serverInstanceConfig) {
        return execCommand(Command.STOP,serverInstanceConfig);
    }

    public Result deleteServer(ServerInstanceConfig serverInstanceConfig) {
        return execCommand(Command.DELETE,serverInstanceConfig);
    }

    public Result execCommand(Command command, ServerInstanceConfig serverInstanceConfig){
        String shell = null;
        switch (command){
            case INIT_SERVER:{
                shell = INIT_SERVER_SHELL;
                break;
            }
            case DEPLOY: {
                shell = DEPLOY_SERVER_SHELL;
                break;
            }
            case RESTART: {
                shell = DEPLOY_SERVER_SHELL;
                break;
            }
            case START: {
                shell = DEPLOY_SERVER_SHELL;
                break;
            }
            case STOP: {
                shell = DEPLOY_SERVER_SHELL;
                break;
            }
            case DELETE: {
                shell = DEPLOY_SERVER_SHELL;
                break;
            }
        }
        Map params = new HashMap<>();
        params.put("server_name",serverInstanceConfig.getServerName());
        params.put("main_args", StringUtils.trimToEmpty(serverInstanceConfig.getMainArgs()));
        params.put("jvm_args",serverInstanceConfig.getJvmArgs());
        params.put("main_class",serverInstanceConfig.getMainClass());
        params.put("source_path",serverInstanceConfig.getSourcePath());
        params.put("shell_home",SHELL_PATH);
        final String[] strings = ShellUtil.exec(new String[]{SHELL_PATH + shell,command.toString().toLowerCase()}, params);
        final Result result = Result.builder().success("0".equals(strings[0])).message(strings[1]).build();
        return result;

    }
}
