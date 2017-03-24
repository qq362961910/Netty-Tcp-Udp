package com.wxsk.data.collector.netty.protocol.analyser;

import com.wxsk.data.base.analyser.protocol.ProtocolAnalyser;
import com.wxsk.data.base.analyser.protocol.ProtocolAnalyserManager;

import java.util.List;

public class NettyProtocolAnalyserManager implements ProtocolAnalyserManager {

    private List<ProtocolAnalyser> protocolAnalyserList;

    @Override
    public ProtocolAnalyser selectMessageAnalyser(Object message) {
        if (protocolAnalyserList == null || protocolAnalyserList.isEmpty()) {
            return null;
        }
        for (ProtocolAnalyser analyser: protocolAnalyserList) {

        }
        return null;
    }

    public List<ProtocolAnalyser> getProtocolAnalyserList() {
        return protocolAnalyserList;
    }

    public NettyProtocolAnalyserManager setProtocolAnalyserList(List<ProtocolAnalyser> protocolAnalyserList) {
        this.protocolAnalyserList = protocolAnalyserList;
        return this;
    }
}
