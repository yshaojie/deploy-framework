package com.self.deploy.web.service;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.common.base.Preconditions;
import com.self.deploy.web.bean.InstanceGroup;
import com.self.deploy.web.bean.ServerInstance;
import com.self.deploy.web.common.Command;
import com.self.deploy.web.repository.ServerInstanceRepository;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by shaojieyue
 * Created time 2017-02-24 16:01
 */

@Service
public class ServerInstanceService {
    private static final Logger logger = LoggerFactory.getLogger(ServerInstanceService.class);
    private final static ObjectMapper mapper = newMapper();

    @Resource
    private ServerInstanceRepository serverInstanceRepository;

    @Resource
    private InstanceGroupService instanceGroupService;
    public static final String PACKAGE_HTTP_ROOT = "http://127.0.0.1:8080/";
    public static final String TARGET_INIT_SERVER_URL="http://%s:9001/agent/command/init_server";
    public static final String TARGET_DEPLOY_URL="http://%s:9001/agent/command/deploy";
    public static final String TARGET_RESTART_URL="http://%s:9001/agent/command/restart";
    public static final String TARGET_START_URL="http://%s:9001/agent/command/start";
    public static final String TARGET_STOP_URL="http://%s:9001/agent/command/stop";
    public static final String TARGET_DELETE_URL="http://%s:9001/agent/command/delete";

    private static final CloseableHttpClient httpclient = HttpClients.createDefault();
    public List<ServerInstance> findByGroupId(int groupId) {
        return serverInstanceRepository.findByInstanceGroupId(groupId);
    }

    public List<String> deploy(List<Integer> serverInstanceIds) {
        return batchRequest(serverInstanceIds,Command.DEPLOY);
    }

    public List<String> restart(List<Integer> serverInstanceIds) {
        return batchRequest(serverInstanceIds,Command.RESTART);
    }

    public List<String> start(List<Integer> serverInstanceIds) {
        return batchRequest(serverInstanceIds,Command.START);
    }

    public List<String> stop(List<Integer> serverInstanceIds) {
        return batchRequest(serverInstanceIds,Command.STOP);
    }

    public List<String> delete(List<Integer> serverInstanceIds) {
        return batchRequest(serverInstanceIds,Command.DELETE);
    }

    public List<String> batchRequest(List<Integer> serverInstanceIds, Command command) {
        List<String> results = new ArrayList<>();
        final Iterable<ServerInstance> serverInstances = serverInstanceRepository.findAll(serverInstanceIds);
        for (ServerInstance serverInstance : serverInstances) {
            InstanceGroup group = instanceGroupService.findById(serverInstance.getInstanceGroupId());
            logger.info("begin {} server serverName={},ip={}",command,group.getServerName(),serverInstance.getIp());
            String url = null;
            switch (command){
                case DEPLOY: {
                    url = String.format(TARGET_DEPLOY_URL,serverInstance.getIp());
                    break;
                }
                case RESTART: {
                    url = String.format(TARGET_RESTART_URL,serverInstance.getIp());
                    break;
                }
                case START: {
                    url = String.format(TARGET_START_URL,serverInstance.getIp());
                    break;
                }
                case STOP: {
                    url = String.format(TARGET_STOP_URL,serverInstance.getIp());
                    break;
                }
                case DELETE: {
                    url = String.format(TARGET_DELETE_URL,serverInstance.getIp());
                    break;
                }
            }
            String message = "";
            try {
                message = execAction(group, url);
            }catch (Exception e){
                message = "发布出现未知错误";
                logger.error("ex",e);
            }
            results.add(message);
            logger.info("end {} server,result={} serverName={},ip={}",command,message,group.getServerName(),serverInstance.getIp());
        }

        return results;
    }

    private String execAction(InstanceGroup group, String url) {
        HttpPut httpPut = new HttpPut(url);
        StringEntity stringEntity = null;
        try {
            stringEntity = new StringEntity(mapper.writeValueAsString(group), ContentType.APPLICATION_JSON);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        httpPut.setEntity(stringEntity);
        CloseableHttpResponse httpResponse = null;
        try {
            httpResponse = httpclient.execute(httpPut);
        } catch (IOException e) {
            throw new RuntimeException("exec fail.",e);
        }
        final String result;
        try {
            result = EntityUtils.toString(httpResponse.getEntity());
        } catch (IOException e) {
            throw new RuntimeException("exec fail.",e);
        }

        //发送请求失败
        if (httpResponse.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
            logger.info("send sms fail. http statusCode={},result={}",httpResponse.getStatusLine().getStatusCode(),result);
            return "";
        }
        return "";
    }

    @Transactional
    public String addServerInstance(int instanceGroupId, String ip) {
        final InstanceGroup instanceGroup = instanceGroupService.findById(instanceGroupId);
        Preconditions.checkNotNull(instanceGroup,"ip="+ip+" is not exist at InstanceGroup");
        final ServerInstance serverInstance = ServerInstance.builder()
                .instanceGroupId(instanceGroup.getId())
                .ip(ip)
                .build();
        serverInstanceRepository.save(serverInstance);
        final String url = String.format(TARGET_INIT_SERVER_URL, serverInstance.getIp());
        instanceGroup.setSourceName("http://localhost:8080/dist/"+instanceGroup.getSourceName());
        final String result = execAction(instanceGroup, url);
        return result;
    }

    private static final ObjectMapper newMapper(){
        ObjectMapper objectMapper = new ObjectMapper();
        // 允许单引号
        objectMapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
        // 允许反斜杆等字符
        objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
        // 允许出现对象中没有的字段
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true) ;
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        //不进行格式化打印
        objectMapper.disable(SerializationFeature.INDENT_OUTPUT);
        objectMapper.configure(JsonGenerator.Feature.AUTO_CLOSE_TARGET, false);
        objectMapper.configure(JsonGenerator.Feature.AUTO_CLOSE_JSON_CONTENT, false);
        objectMapper.disable(SerializationFeature.FLUSH_AFTER_WRITE_VALUE);
        objectMapper.disable(SerializationFeature.CLOSE_CLOSEABLE);
        objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        return objectMapper;
    }
}
