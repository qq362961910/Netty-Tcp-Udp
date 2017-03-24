package com.wxsk.data.base.enums;

public enum MessageProtocol {

    /**
     * YTWS
     * */
    YTWS(0xFA, "YTWS"),

    /**
     * CFS
     * */
    CFS('$', "CFS"),

    /**
     * 立安
     * */
    LI_AN('@', "LI_AN"),

    /**
     * EVENT MESSAGE
     * */
    EVENT_MESSAGE(1, "EVENT_MESSAGE"),

    /**
     * String
     * */
    STRING(Integer.MAX_VALUE, "String");

    public final int value;
    public final String name;

    public static void main(String[] args) {
        System.out.println((char)0xFA);
    }


    public static MessageProtocol getMessageProtocol(int value) {
        for (MessageProtocol protocol:  values()) {
            if (protocol.value == value) {
                return protocol;
            }
        }
        return null;
    }

    MessageProtocol(int value, String name) {
        this.value = value;
        this.name = name;
    }

    @Override
    public String toString() {
        return "MessageProtocol{" +
                "value=" + value +
                ", name='" + name + '\'' +
                '}';
    }
}
