package com.self.deploy.web.service;

import com.self.deploy.web.bean.InstanceGroup;
import com.self.deploy.web.repository.InstanceGroupRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by shaojieyue
 * Created time 2017-02-24 11:53
 */

@Service
public class InstanceGroupService {
    private static final Logger logger = LoggerFactory.getLogger(InstanceGroupService.class);

    @Resource
    private InstanceGroupRepository instanceGroupRepository;

    public List<InstanceGroup> findAll() {
        final Iterable<InstanceGroup> instanceGroups = instanceGroupRepository.findAll();
        List<InstanceGroup> groups = new ArrayList<>();
        instanceGroups.forEach(instanceGroup -> groups.add(instanceGroup));
        return groups;
    }

    public InstanceGroup findById(int instanceGroupId) {
        return instanceGroupRepository.findOne(instanceGroupId);
    }
}
