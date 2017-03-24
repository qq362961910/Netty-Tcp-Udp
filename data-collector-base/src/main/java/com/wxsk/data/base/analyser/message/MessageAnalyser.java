package com.wxsk.data.base.analyser.message;

import com.wxsk.data.base.enums.MessageProtocol;

/**
 * 消息解析器
 * */
public interface MessageAnalyser<In, Out> {

    /**
     * 是否支持解析该协议
     * */
    boolean support(MessageProtocol protocol);

    /**
     * 解析消息
     * */
    Out analyse(In t);

}
