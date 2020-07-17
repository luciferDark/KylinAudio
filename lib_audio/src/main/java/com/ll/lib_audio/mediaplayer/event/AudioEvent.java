package com.ll.lib_audio.mediaplayer.event;

import com.ll.lib_audio.mediaplayer.bean.AudioBean;
import com.ll.lib_audio.mediaplayer.core.AudioController;

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
    public AudioController.PlayMode playMode;

    public AudioEvent(Status eventCode, String eventMsg) {
        this.eventCode = eventCode;
        this.eventMsg = eventMsg;
    }

    public AudioEvent(Status eventCode, String eventMsg, AudioBean audioBean) {
        this.eventCode = eventCode;
        this.eventMsg = eventMsg;
        this.audioBean = audioBean;
    }

    public AudioEvent(Status eventCode, String eventMsg, AudioBean audioBean, AudioController.PlayMode playMode) {
        this.eventCode = eventCode;
        this.eventMsg = eventMsg;
        this.audioBean = audioBean;
        this.playMode = playMode;
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
        EVENT_ADD_FAVOURITE,
        EVENT_REMOVE_FAVOURITE,
        EVENT_LOAD_ERROR,
        EVENT_PLAY_MODE,
    }

    @Override
    public String toString() {
        return "AudioEvent{" +
                "eventCode=" + eventCode +
                ", eventMsg='" + eventMsg + '\'' +
                ", audioBean=" + audioBean +
                ", playMode=" + playMode +
                '}';
    }
}
