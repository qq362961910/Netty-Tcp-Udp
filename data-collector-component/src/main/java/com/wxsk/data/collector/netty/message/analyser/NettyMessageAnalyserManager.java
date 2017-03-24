package com.wxsk.data.collector.netty.message.analyser;

import com.wxsk.data.base.analyser.message.MessageAnalyser;
import com.wxsk.data.base.analyser.message.MessageAnalyserManager;
import com.wxsk.data.base.enums.MessageProtocol;

import java.util.List;

public class NettyMessageAnalyserManager implements MessageAnalyserManager {

    private List<MessageAnalyser> messageAnalyserList;

    @Override
    public MessageAnalyser selectMessageAnalyser(MessageProtocol protocol) {
        if (messageAnalyserList == null || messageAnalyserList.isEmpty()) {
            return null;
        }
        for (MessageAnalyser analyser: messageAnalyserList) {
            if (analyser.support(protocol)) {
                return analyser;
            }
        }
        return null;
    }

    public List<MessageAnalyser> getMessageAnalyserList() {
        return messageAnalyserList;
    }

    public NettyMessageAnalyserManager setMessageAnalyserList(List<MessageAnalyser> messageAnalyserList) {
        this.messageAnalyserList = messageAnalyserList;
        return this;
    }
}
