package com.wxsk.data.base.analyser.protocol;

public interface ProtocolAnalyserManager {

    ProtocolAnalyser selectMessageAnalyser(Object message);
}
