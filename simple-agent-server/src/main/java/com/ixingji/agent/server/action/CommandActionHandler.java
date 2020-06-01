package com.ixingji.agent.server.action;

import com.alibaba.fastjson.JSON;
import com.ixingji.agent.server.util.CmdUtils;

public class CommandActionHandler implements ActionHandler {

    private final String command;

    public CommandActionHandler(String command) {
        this.command = command;
    }

    @Override
    public String doHandle(String requestData) throws Exception {
        return JSON.toJSONString(CmdUtils.exec(command));
    }

}
