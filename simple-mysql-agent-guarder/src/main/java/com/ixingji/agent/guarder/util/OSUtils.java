package com.ixingji.agent.guarder.util;

import org.apache.commons.lang3.StringUtils;
import org.omg.CORBA.UNKNOWN;

public class OSUtils {

    enum OS {
        UNKNOWN,
        WINDOWS,
        LINUX;
    }

    public static OS os() {
        String osName = System.getenv("os.name").toUpperCase();
        if (StringUtils.contains(osName, OS.WINDOWS.name())) {
            return OS.WINDOWS;
        } else if (StringUtils.contains(osName, OS.LINUX.name())) {
            return OS.LINUX;
        }
        return OS.UNKNOWN;
    }

}
