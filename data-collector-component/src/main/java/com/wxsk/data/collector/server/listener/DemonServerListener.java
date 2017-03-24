package com.wxsk.data.collector.server.listener;

import com.wxsk.data.collector.server.DaemonServer;

public interface DemonServerListener {

    void startup(DaemonServer server);

    void close(DaemonServer server);
}
