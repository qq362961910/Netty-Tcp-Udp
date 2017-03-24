package com.wxsk.data.collector.launcher;

import com.wxsk.data.collector.server.DaemonServer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DefaultLauncher extends AbstractLauncher{

    private static final Logger logger = LogManager.getLogger(DefaultLauncher.class);

    public void doStart() {
        //启动所有服务器
        if (getDaemonServerList() != null && !getDaemonServerList().isEmpty()) {
            logger.info(String.format("server list size: %d", getDaemonServerList().size()));
            for (final DaemonServer server: getDaemonServerList()) {
                startServer(server);
            }
        }
    }

    public void doClose() {
        //启动所有服务器
        if (getDaemonServerList() != null && !getDaemonServerList().isEmpty()) {
            for (DaemonServer server: getDaemonServerList()) {
                server.close(this);
            }
        }
    }

}
