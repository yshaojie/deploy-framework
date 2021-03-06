package com.self.deploy.web.bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Created by shaojieyue
 * Created time 2017-02-24 15:58
 */

@Data
@NoArgsConstructor(force = true)
@AllArgsConstructor
@Builder
@Entity
public class ServerInstance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String ip;//服务ip
    private int status;//服务状态
    private int instanceGroupId;//所属实例组
    private String deployTime;//部署时间
    private String deployBy;//最后部署人
}
