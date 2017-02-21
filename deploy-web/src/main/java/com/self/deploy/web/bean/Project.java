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
 * Created by shaojieyue
 * Created time 2017-02-21 11:27
 */

@Data
@NoArgsConstructor(force = true)
@AllArgsConstructor
@Builder
@Entity
public class Project {

    @Id
    @GenericGenerator(name = "idGenerator", strategy = "identity")
    @GeneratedValue(generator = "idGenerator")
    private int id;
    private String url;
    private Date createDate;
    private String createBy;
    private String remark;
    private String currentTag;
}
