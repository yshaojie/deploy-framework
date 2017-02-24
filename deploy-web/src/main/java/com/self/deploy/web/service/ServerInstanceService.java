package com.self.deploy.web.service;

import com.self.deploy.web.bean.InstanceGroup;
import com.self.deploy.web.bean.ServerInstance;
import com.self.deploy.web.common.Command;
import com.self.deploy.web.repository.ServerInstanceRepository;
import org.apache.http.HttpStatus;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by shaojieyue
 * Created time 2017-02-24 16:01
 */

@Service
public class ServerInstanceService {
    private static final Logger logger = LoggerFactory.getLogger(ServerInstanceService.class);

    @Resource
    private ServerInstanceRepository serverInstanceRepository;

    @Resource
    private InstanceGroupService instanceGroupService;
    public static final String PACKAGE_HTTP_ROOT = "http://127.0.0.1:8080/";
    public static final String TARGET_DEPLOY_URL="http://%s:12457/agent/deploy";
    public static final String TARGET_RESTART_URL="http://%s:12457/agent/restart";
    public static final String TARGET_START_URL="http://%s:12457/agent/start";
    public static final String TARGET_STOP_URL="http://%s:12457/agent/stop";
    public static final String TARGET_DELETE_URL="http://%s:12457/agent/delete";

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
        final ArrayList<BasicNameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("serverNmae",group.getServerName()));
        params.add(new BasicNameValuePair("package_path",PACKAGE_HTTP_ROOT+"/"+group.getSourceName()));
        UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(params, Charset.defaultCharset());
        httpPut.setEntity(urlEncodedFormEntity);
        CloseableHttpResponse httpResponse = null;
        try {
            httpResponse = httpclient.execute(httpPut);
        } catch (IOException e) {
            throw new RuntimeException("send sms fail.",e);
        }
        final String result;
        try {
            result = EntityUtils.toString(httpResponse.getEntity());
        } catch (IOException e) {
            throw new RuntimeException("send sms fail.",e);
        }

        //发送请求失败
        if (httpResponse.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
            logger.info("send sms fail. http statusCode={},result={}",httpResponse.getStatusLine().getStatusCode(),result);
            return "";
        }
        return "";
    }
}
