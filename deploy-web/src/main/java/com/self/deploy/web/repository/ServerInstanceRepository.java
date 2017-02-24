package com.self.deploy.web.repository;

import com.self.deploy.web.bean.ServerInstance;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by shaojieyue
 * Created time 2017-02-21 11:30
 */

@Repository
public interface ServerInstanceRepository extends CrudRepository<ServerInstance,Integer> {

    List<ServerInstance> findByInstanceGroupId(int groupId);
}
