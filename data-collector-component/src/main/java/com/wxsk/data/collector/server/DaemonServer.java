package com.wxsk.data.collector.server;

import com.wxsk.data.collector.launcher.Launcher;

/**
 * 守护服务
 * */
public interface DaemonServer {

    void start(Launcher launcher);

    void close(Launcher launcher);

}
