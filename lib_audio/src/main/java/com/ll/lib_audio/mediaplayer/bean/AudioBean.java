package com.ll.lib_audio.mediaplayer.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Generated;

import java.io.Serializable;

/**
 * @Auther Kylin
 * @Data 2020/5/21 - 14:33
 * @Package com.ll.lib_audio.mediaplayer.bean
 * @Description 歌曲实体类
 */
@Entity
public class AudioBean implements Serializable {
    private static final long serialVersionUID = -8849228294348905620L;

    @Id
    private String id;
    @NotNull
    private String name;
    @NotNull
    private String url;
    @NotNull
    private String singer;
    private String singerInfo;
    @NotNull
    private String album;
    @NotNull
    private String albumInfo;
    @NotNull
    private String albumPic;
    @NotNull
    private String totalTime;

    @Generated(hash = 1858738008)
    public AudioBean(String id, @NotNull String name, @NotNull String url,
            @NotNull String singer, String singerInfo, @NotNull String album,
            @NotNull String albumInfo, @NotNull String albumPic,
            @NotNull String totalTime) {
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
    @Generated(hash = 1628963493)
    public AudioBean() {
    }
    public String getId() {
        return this.id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getUrl() {
        return this.url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public String getSinger() {
        return this.singer;
    }
    public void setSinger(String singer) {
        this.singer = singer;
    }
    public String getSingerInfo() {
        return this.singerInfo;
    }
    public void setSingerInfo(String singerInfo) {
        this.singerInfo = singerInfo;
    }
    public String getAlbum() {
        return this.album;
    }
    public void setAlbum(String album) {
        this.album = album;
    }
    public String getAlbumInfo() {
        return this.albumInfo;
    }
    public void setAlbumInfo(String albumInfo) {
        this.albumInfo = albumInfo;
    }
    public String getAlbumPic() {
        return this.albumPic;
    }
    public void setAlbumPic(String albumPic) {
        this.albumPic = albumPic;
    }
    public String getTotalTime() {
        return this.totalTime;
    }
    public void setTotalTime(String totalTime) {
        this.totalTime = totalTime;
    }

    @Override
    public String toString() {
        return "AudioBean{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", url='" + url + '\'' +
                ", singer='" + singer + '\'' +
                ", singerInfo='" + singerInfo + '\'' +
                ", album='" + album + '\'' +
                ", albumInfo='" + albumInfo + '\'' +
                ", albumPic='" + albumPic + '\'' +
                ", totalTime='" + totalTime + '\'' +
                '}';
    }
}
