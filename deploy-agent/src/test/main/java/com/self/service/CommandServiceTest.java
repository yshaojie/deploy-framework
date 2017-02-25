package com.self.service;

import com.self.agent.AgentApplication;
import com.self.agent.bean.ServerInstanceConfig;
import com.self.agent.service.CommandService;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

/**
 * Created by shaojieyue
 * Created time 2017-02-25 17:14
 */

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = AgentApplication.class)
public class CommandServiceTest {
    private static final Logger logger = LoggerFactory.getLogger(CommandServiceTest.class);

    @Resource
    private CommandService commandService;

    @BeforeClass
    public static void init(){
        System.setProperty("resources","/home/shaojieyue/tools/workspace/deploy-framework/deploy-agent/src/main/resources");
    }

    @Test
    public void initServerTest(){

        final ServerInstanceConfig instanceConfig = ServerInstanceConfig.builder()
                .jvmArgs("-server -Xmx512m -Xms512m -Xss256k")
                .mainArgs("")
                .mainClass("com.goabroad.user.UserWebApplication")
                .serverName("user-web-server")
                .sourceName("http://localhost:8080/dist/http://localhost:8080/dist/http://localhost:8080/dist/http://localhost:8080/dist/http://localhost:8080/dist/user-web-server-dist.zip")
                .build();
        final String server = commandService.initServer(instanceConfig);
    }
}
