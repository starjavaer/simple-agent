package com.ixingji.agent.server.action;

import java.util.HashMap;
import java.util.Map;

public final class ActionManager {

    private static Map<String, ActionHandler> handlerMap = new HashMap<>();

    public static void registerHandler(String name, ActionHandler actionHandler) {
        handlerMap.put(name, actionHandler);
    }

    public static ActionHandler getHandler(String name) {
        return handlerMap.get(name);
    }

    private ActionManager() {

    }

}
