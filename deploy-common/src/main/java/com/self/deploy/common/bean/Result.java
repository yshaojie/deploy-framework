package com.self.deploy.common.bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by shaojieyue
 * Created time 2017-02-26 09:50
 */

@Data
@NoArgsConstructor(force = true)
@AllArgsConstructor
@Builder
public class Result {
    private String ip;
    private boolean success;
    private String message;
}
