package com.self.deploy.web.controller;

import com.self.deploy.common.bean.Result;
import com.self.deploy.web.bean.ServerInstance;
import com.self.deploy.web.service.ServerInstanceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

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
        return serverInstanceService.addServerInstance(instanceGroupId,ip);
    }

    @RequestMapping(value = "deploy",method = RequestMethod.PUT)
    public Object deploy(@RequestParam List<Integer> serverInstanceIds){
        List<Result> results = serverInstanceService.deploy(serverInstanceIds);
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
