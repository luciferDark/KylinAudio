package com.ll.lib_audio.mediaplayer.event;

import com.ll.lib_audio.mediaplayer.bean.AudioBean;

/**
 * @Auther Kylin
 * @Data 2020/6/23 - 4:13
 * @Package com.ll.lib_audio.mediaplayer.event
 * @Description
 */
public class AudioEvent {
    public Status eventCode;
    public String eventMsg;
    public AudioBean audioBean;

    public AudioEvent(Status eventCode, String eventMsg) {
        this.eventCode = eventCode;
        this.eventMsg = eventMsg;
    }

    public AudioEvent(Status eventCode, String eventMsg, AudioBean audioBean) {
        this.eventCode = eventCode;
        this.eventMsg = eventMsg;
        this.audioBean = audioBean;
    }

    public enum Status{
        EVENT_LOAD,
        EVENT_START,
        EVENT_PASUE,
        EVENT_RESUME,
        EVENT_STOP,
        EVENT_COMPLETION,
        EVENT_RELEASE,
        EVENT_ERROR,
        EVENT_LOAD_ERROR,
    }
}
