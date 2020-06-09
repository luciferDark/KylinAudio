package com.ll.lib_audio.mediaplayer.exception;

/**
 * @Auther Kylin
 * @Data 2020/6/9 - 16:12
 * @Package com.ll.lib_audio.mediaplayer.exception
 * @Description
 */
public class AudioContextNullException extends RuntimeException {
    public String errorMsg = "please call the AudioHelp to init the Context";

    public AudioContextNullException() {
    }

    public AudioContextNullException(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}
