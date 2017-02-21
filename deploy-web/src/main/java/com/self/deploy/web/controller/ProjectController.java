package com.self.deploy.web.controller;

import com.self.deploy.web.bean.Project;
import com.self.deploy.web.service.ProjectService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

/**
 * Created by shaojieyue
 * Created time 2017-02-21 11:32
 */


@RestController
@RequestMapping(value = "/project", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class ProjectController {
    private static final Logger logger = LoggerFactory.getLogger(ProjectController.class);

    @Resource
    private ProjectService projectService;

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public Object create(String url){
        Project project = projectService.create(url);
        return project;
    }

    @RequestMapping(value = "list",method = RequestMethod.GET)
    @ResponseBody
    public Object list(){
        Iterable<Project> list = projectService.findAll();
        return list;
    }

    @RequestMapping(value = "package",method = RequestMethod.PUT)
    @ResponseBody
    public Object pack(String branch,int projectId,String module){
        return projectService.pack(projectId,module,branch);
    }

    @RequestMapping(value = "detail",method = RequestMethod.GET)
    @ResponseBody
    public Object detail(int projectId){
        Map detail = projectService.findDetail(projectId);
        return detail;
    }
}
