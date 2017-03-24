package com.wxsk.data.collector.launcher;

import com.wxsk.data.collector.server.DaemonServer;

public interface Launcher {

    void startup();

    void close();

    void serverStartSuccess(DaemonServer server);

    void serverShutdownSuccess(DaemonServer server);
}
