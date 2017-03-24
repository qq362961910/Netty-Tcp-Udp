package com.wxsk.data.base.analyser.message;


import com.wxsk.data.base.enums.MessageProtocol;

public interface MessageAnalyserManager {

    MessageAnalyser selectMessageAnalyser(MessageProtocol protocol);
}
