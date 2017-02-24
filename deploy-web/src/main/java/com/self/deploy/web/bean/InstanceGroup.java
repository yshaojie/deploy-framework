package com.self.deploy.web.bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

/**
 * 创建实例组
 * Created by shaojieyue
 * Created time 2017-02-24 11:36
 */
@Data
@NoArgsConstructor(force = true)
@AllArgsConstructor
@Builder
@Entity
public class InstanceGroup {

    @Id
    @GenericGenerator(name = "idGenerator", strategy = "identity")
    @GeneratedValue(generator = "idGenerator")
    private int id;
    private String mainClass;
    private String mainArgs;
    private String jvmArgs;
    private String sourceName;
    private String serverName;
    private Date createDate;
    private String remark;
}
