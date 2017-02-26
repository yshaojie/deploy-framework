package com.self.agent.controller;

import com.self.agent.service.CommandService;
import com.self.deploy.common.bean.Result;
import com.self.deploy.common.bean.ServerInstanceConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * Created by shaojieyue
 * Created time 2017-02-24 19:10
 */

@RestController
@RequestMapping("/command")
public class CommandController {
    private static final Logger logger = LoggerFactory.getLogger(CommandController.class);

    @Resource
    private CommandService commandService;

    @RequestMapping(value = "init_server")
    public  Result initServer(@RequestBody ServerInstanceConfig serverInstanceConfig){
        Result result = commandService.initServer(serverInstanceConfig);
        return result;
    }

    @RequestMapping(value = "deploy")
    public  Result deploy(@RequestBody ServerInstanceConfig serverInstanceConfig){
        Result result = commandService.deployServer(serverInstanceConfig);
        return result;
    }

    @RequestMapping(value = "restart")
    public  Result restart(@RequestBody ServerInstanceConfig serverInstanceConfig){
        Result result = commandService.deployServer(serverInstanceConfig);
        return result;
    }

    @RequestMapping(value = "start")
    public  Result start(@RequestBody ServerInstanceConfig serverInstanceConfig){
        Result result = commandService.deployServer(serverInstanceConfig);
        return result;
    }

    @RequestMapping(value = "stop")
    public  Result stop(@RequestBody ServerInstanceConfig serverInstanceConfig){
        Result result = commandService.deployServer(serverInstanceConfig);
        return result;
    }

    @RequestMapping(value = "delete")
    public  Result delete(@RequestBody ServerInstanceConfig serverInstanceConfig){
        Result result = commandService.deployServer(serverInstanceConfig);
        return result;
    }

}
