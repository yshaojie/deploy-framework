package com.self.deploy.web.controller;

import com.google.common.collect.Maps;
import com.self.deploy.web.bean.ServerInstance;
import com.self.deploy.web.service.ServerInstanceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * Created by shaojieyue
 * Created time 2017-02-24 16:02
 */

@RestController
@RequestMapping(value = "/instance", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class ServerInstanceController {
    private static final Logger logger = LoggerFactory.getLogger(ServerInstanceController.class);

    @Resource
    private ServerInstanceService serverInstanceService;

    @RequestMapping("list")
    public List<ServerInstance> list(int groupId){
        List<ServerInstance> instances = serverInstanceService.findByGroupId(groupId);
        return instances;
    }

    @RequestMapping(value = "add",method = RequestMethod.PUT)
    public Object addServerInstance(int instanceGroupId, String ip){
        String message = serverInstanceService.addServerInstance(instanceGroupId,ip);
        Map data = Maps.newHashMap();
        data.put("message",message);
        return data;
    }

    @RequestMapping(value = "deploy",method = RequestMethod.PUT)
    public Object deploy(@RequestParam List<Integer> serverInstanceIds){
        logger.info("obj={}",serverInstanceIds);
        List<String> results = serverInstanceService.deploy(serverInstanceIds);
        return results;
    }


    @RequestMapping(value = "restart",method = RequestMethod.PUT)
    public Object restart(@RequestParam List<Integer> serverInstanceIds){
        logger.info("obj={}",serverInstanceIds);
        return "";
    }

    @RequestMapping(value = "start",method = RequestMethod.PUT)
    public Object start(@RequestParam List<Integer> serverInstanceIds){
        logger.info("obj={}",serverInstanceIds);
        return "";
    }

    @RequestMapping(value = "stop",method = RequestMethod.PUT)
    public Object stop(@RequestParam List<Integer> serverInstanceIds){
        logger.info("obj={}",serverInstanceIds);
        return "";
    }

    @RequestMapping(value = "delete",method = RequestMethod.DELETE)
    public Object delete(@RequestParam List<Integer> serverInstanceIds){
        logger.info("obj={}",serverInstanceIds);
        return "";
    }

}
