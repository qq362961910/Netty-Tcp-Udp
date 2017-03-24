package com.wxsk.data.collector.launcher;

import com.wxsk.data.collector.launcher.listener.LauncherListener;
import com.wxsk.data.collector.server.DaemonServer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class AbstractLauncher implements Launcher{

    private AtomicInteger serverSuccessCount = new AtomicInteger(0);
    private static final Logger logger = LogManager.getLogger(AbstractLauncher.class);
    private List<DaemonServer> daemonServerList;
    private List<LauncherListener> launcherListenerList;
    private ExecutorService executorService = Executors.newFixedThreadPool(5);

    /**
     * 启动
     * */
    public void startup() {
        logger.info("launcher begin to startup");
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            close();
        }));
        doStart();
        while (daemonServerList != null && serverSuccessCount.get() != daemonServerList.size()) {
            try { Thread.sleep(500); } catch (InterruptedException e) {}
        }
        logger.info("launcher startup successfully");
        //回调监听器
        if (launcherListenerList != null && !launcherListenerList.isEmpty()) {
            logger.info(String.format("launcher listener size: %d, begin to call launcher listeners",launcherListenerList.size()));
            for (LauncherListener listener: launcherListenerList) {
                listener.startup(this);
            }
        }
    }

    public abstract void doStart();

    /**
     * 关闭
     * */
    public void close() {
        logger.info("launcher begin to shutdown");
        doClose();
        while (serverSuccessCount.get() == 0) {
            try { Thread.sleep(500); } catch (InterruptedException e) {}
        }
        executorService.shutdown();
        try {
            executorService.awaitTermination(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            logger.error(e);
            logger.info("shutdownnow run");
            System.out.println("shutdownnow run");
            executorService.shutdownNow();
        }
        logger.info("launcher shutdown successfully");
        System.out.println("launcher shutdown successfully");
        //回调监听器
        if (launcherListenerList != null && !launcherListenerList.isEmpty()) {
            for (LauncherListener listener: launcherListenerList) {
                listener.close(this);
            }
        }
    }

    public void startServer(DaemonServer server) {
        executorService.submit(() ->{
            server.start(this);
        });
    }

    public abstract void doClose();

    @Override
    public void serverStartSuccess(DaemonServer server) {
        serverSuccessCount.addAndGet(1);
    }

    @Override
    public void serverShutdownSuccess(DaemonServer server) {
        serverSuccessCount.addAndGet(-1);
    }

    public List<DaemonServer> getDaemonServerList() {
        return daemonServerList;
    }

    public AbstractLauncher setDaemonServerList(List<DaemonServer> daemonServerList) {
        this.daemonServerList = daemonServerList;
        return this;
    }

    public List<LauncherListener> getLauncherListenerList() {
        return launcherListenerList;
    }

    public AbstractLauncher setLauncherListenerList(List<LauncherListener> launcherListenerList) {
        this.launcherListenerList = launcherListenerList;
        return this;
    }
}
