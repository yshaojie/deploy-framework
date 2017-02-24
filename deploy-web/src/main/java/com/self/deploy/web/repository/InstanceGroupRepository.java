package com.self.deploy.web.repository;

import com.self.deploy.web.bean.InstanceGroup;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by shaojieyue
 * Created time 2017-02-21 11:30
 */

@Repository
public interface InstanceGroupRepository extends CrudRepository<InstanceGroup,Integer> {

}
