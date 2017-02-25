package com.self.agent.controller;

import com.self.agent.bean.ServerInstanceConfig;
import com.self.agent.service.CommandService;
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
    public String initServer(@RequestBody ServerInstanceConfig serverInstanceConfig){
        String result = commandService.initServer(serverInstanceConfig);
        return result;
    }

}
