package com.wxsk.data.collector.server;

import com.wxsk.data.collector.launcher.Launcher;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class AbstractDaemonServer implements DaemonServer {

    private static final Logger logger = LogManager.getLogger(AbstractDaemonServer.class);

    private String name = toString();
    private final int port;

    @Override
    public void start(Launcher launcher) {
        doStart(launcher);
    }

    /**
     * 服务器会阻塞在这里
     * */
    public abstract void doStart(Launcher launcher);

    @Override
    public void close(Launcher launcher) {
        doClose(launcher);
    }

    public abstract void doClose(Launcher launcher);

    public AbstractDaemonServer(int port) {
        this.port = port;
    }

    public AbstractDaemonServer(String name, int port) {
        this.name = name;
        this.port = port;
    }

    public String getName() {
        return name;
    }

    public AbstractDaemonServer setName(String name) {
        this.name = name;
        return this;
    }

    public int getPort() {
        return port;
    }
}
