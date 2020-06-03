package com.ixingji.agent.guarder.model;

import lombok.Data;

@Data
public class RelateZKRequest {

    private String zkUrl;

    private String secretId;

    private Integer serverPort;

    // 可以补充一些其它信息
    // dbServerZkNodePath

}
