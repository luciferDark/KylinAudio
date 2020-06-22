package com.ll.lib_audio.mediaplayer.bean;

import org.jetbrains.annotations.NotNull;

/**
 * @Auther Kylin
 * @Data 2020/5/21 - 14:33
 * @Package com.ll.lib_audio.mediaplayer.bean
 * @Description 歌曲实体类
 */
public class AudioBean {
    private String id;
    private String name;
    private String url;
    private String singer;
    private String singerInfo;
    private String album;
    private String albumInfo;
    private String albumPic;
    private String totalTime;

    public AudioBean(String id, @NotNull String name, @NotNull String url, @NotNull String singer,  String singerInfo
            , @NotNull String album, @NotNull String albumInfo, @NotNull String albumPic, @NotNull String totalTime) {
        this.id = id;
        this.name = name;
        this.url = url;
        this.singer = singer;
        this.singerInfo = singerInfo;
        this.album = album;
        this.albumInfo = albumInfo;
        this.albumPic = albumPic;
        this.totalTime = totalTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSinger() {
        return singer;
    }

    public void setSinger(String singer) {
        this.singer = singer;
    }

    public String getSingerInfo() {
        return singerInfo;
    }

    public void setSingerInfo(String singerInfo) {
        this.singerInfo = singerInfo;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getAlbumInfo() {
        return albumInfo;
    }

    public void setAlbumInfo(String albumInfo) {
        this.albumInfo = albumInfo;
    }

    public String getAlbumPic() {
        return albumPic;
    }

    public void setAlbumPic(String albumPic) {
        this.albumPic = albumPic;
    }

    public String getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(String totalTime) {
        this.totalTime = totalTime;
    }
}
