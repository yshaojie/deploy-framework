package com.self.agent.service;

import com.self.agent.bean.ServerInstanceConfig;
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

    public String initServer(ServerInstanceConfig serverInstanceConfig) {
        Map params = new HashMap<>();
        params.put("server_name",serverInstanceConfig.getServerName());
        params.put("main_args", StringUtils.trimToEmpty(serverInstanceConfig.getMainArgs()));
        params.put("jvm_args",serverInstanceConfig.getJvmArgs());
        params.put("main_class",serverInstanceConfig.getMainClass());
        params.put("source_path",serverInstanceConfig.getSourceName());
        params.put("shell_home",SHELL_PATH);
        final String[] strings = ShellUtil.exec(new String[]{SHELL_PATH + INIT_SERVER_SHELL}, params);
        return strings[1];
    }
}
