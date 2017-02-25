package com.self.deploy.common.bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by shaojieyue
 * Created time 2017-02-25 13:53
 */

@Data
@NoArgsConstructor(force = true)
@AllArgsConstructor
@Builder
public class ServerInstanceConfig {
    private String mainClass;
    private String mainArgs;
    private String jvmArgs;
    private String sourcePath;
    private String serverName;
}
