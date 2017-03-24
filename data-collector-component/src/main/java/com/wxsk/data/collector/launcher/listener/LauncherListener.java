package com.wxsk.data.collector.launcher.listener;

import com.wxsk.data.collector.launcher.AbstractLauncher;

public interface LauncherListener {

    void startup(AbstractLauncher launcher);

    void close(AbstractLauncher launcher);
}
