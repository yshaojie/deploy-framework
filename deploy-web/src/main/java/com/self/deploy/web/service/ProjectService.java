package com.self.deploy.web.service;

import com.self.deploy.web.bean.Project;
import com.self.deploy.web.repository.ProjectRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

/**
 * Created by shaojieyue
 * Created time 2017-02-21 11:31
 */

@Service
public class ProjectService {
    private static final Logger logger = LoggerFactory.getLogger(ProjectService.class);

    @Resource
    private ProjectRepository projectRepository;


    public Project create(String url) {
        logger.info(" create new project,url = {}",url);
        Project project = Project.builder()
                .url(url)
                .createDate(new Date())
                .remark("")
                .build();
        project = projectRepository.save(project);

        return project;
    }

    public Iterable<Project> findAll() {
        return projectRepository.findAll();
    }
}
