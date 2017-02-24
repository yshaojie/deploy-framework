package com.self.deploy.web.controller;

import com.self.deploy.web.bean.InstanceGroup;
import com.self.deploy.web.service.InstanceGroupService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by shaojieyue
 * Created time 2017-02-24 11:55
 */

@RestController
@RequestMapping(value = "/instance_group", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class InstanceGroupController {
    private static final Logger logger = LoggerFactory.getLogger(InstanceGroupController.class);

    @Resource
    private InstanceGroupService instanceGroupService;

    @RequestMapping(value = "list")
    public Object list(){
        List<InstanceGroup> groups = instanceGroupService.findAll();
        return groups;
    }
}
