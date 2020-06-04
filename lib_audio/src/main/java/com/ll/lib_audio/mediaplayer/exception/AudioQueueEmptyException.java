package com.ll.lib_audio.mediaplayer.exception;

/**
 * @Auther kylin
 * @Data 2020/6/4 - 22:45
 * @Package com.ll.lib_audio.mediaplayer.audio
 * @Description
 */
public class AudioQueueEmptyException extends RuntimeException {
    public String errorMsg;

    public AudioQueueEmptyException() {
    }

    public AudioQueueEmptyException(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}
