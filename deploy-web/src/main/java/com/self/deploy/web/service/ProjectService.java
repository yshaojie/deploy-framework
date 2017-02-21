package com.self.deploy.web.service;

import com.self.deploy.web.bean.Project;
import com.self.deploy.web.repository.ProjectRepository;
import com.self.deploy.web.util.GitUtil;
import com.self.deploy.web.util.ShellUtil;
import com.self.deploy.web.util.XMLUtil;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.Node;
import org.dom4j.XPath;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.*;

/**
 * Created by shaojieyue
 * Created time 2017-02-21 11:31
 */

@Service
public class ProjectService {
    private static final Logger logger = LoggerFactory.getLogger(ProjectService.class);
    public static final String PROJECT_ROOT_HOME = "/data/deploy/projects/";

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

    public String pack(int projectId, String module, String branch) {
        final Project project = projectRepository.findOne(projectId);
        Map<String, String> param = new HashMap<String, String>();
        param.put("project", project.getUrl());
        param.put("branch", branch);
        param.put("module", module);
        final String package_shell = System.getenv("resource") + "/shells/package.sh";
        return ShellUtil.exec(package_shell,param)[1];
    }

    public Map findDetail(int projectId) {
        Map data = new HashMap();
        final Project project = projectRepository.findOne(projectId);
//        git@192.168.100.21:zhuantiku/ztk-question-parent.git
        final String url = project.getUrl();
        final String projectName = StringUtils.substring(url, url.lastIndexOf("/"), url.length() - 4);
        final String projectHome = PROJECT_ROOT_HOME + projectName;
        try {
            final String[] branchs = GitUtil.listBranch(projectHome);
            data.put("branchs",branchs);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (GitAPIException e) {
            e.printStackTrace();
        }
        data.put("modules",queryProjectModules(projectHome));
        return data;
    }

    private List<String> queryProjectModules(String projectHome){
        List<String> modules = new ArrayList<String>();
        //1:操作成功 0:操作失败
        int status = 0;
        String file = projectHome+"/pom.xml";
        Document doc = null;
        try {
            doc = XMLUtil.read(file);
            XPath x=doc.createXPath("/aa:project//aa:modules//aa:module");
            Map map = new HashMap();
            //添加命名空间
            map.put("aa", "http://maven.apache.org/POM/4.0.0");
            x.setNamespaceURIs(map);
            List<Node> nodes = x.selectNodes(doc);
            for(Node node:nodes){
                modules.add(node.getText());
            }
        } catch (Exception e) {
            logger.error("pase pom ex",e);
        }
        return modules;
    }
}
