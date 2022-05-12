package com.example.musicplayer;



public class AudioData {
    private String songName;
    private String artist;
    private String album;
    private String path;
    private int Thumbnail;
    private String date;
    private int id;

    public AudioData(String songName, String path) {
        this.songName=songName;
        this.path=path;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public AudioData(int id,String songName, String path) {
        this.id=id;
        this.songName = songName;
        this.path = path;
    }

    public AudioData() {
    }

    public AudioData(String songName, String aritst, String path, String album) {
        this.artist = aritst;
        this.songName = songName;
        this.path = path;
        this.album = album;
    }

    public AudioData(String songName, String artist, String path, String album, String date) {

        this.songName = songName;
        this.date = date;
        this.album = album;
        this.artist = artist;
        this.path = path;
    }

    public AudioData(int id, String songName, String date, String artist, String path) {
        this.id = id;
        this.songName = songName;
        this.artist = artist;
        this.date = date;
        this.path = path;
    }

    public AudioData(int id, String songName, String artist, String path) {
        this.id = id;
        this.songName = songName;
        this.artist = artist;

        this.path = path;
    }

    public String getSongName() {
        return songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getThumbnail() {
        return Thumbnail;
    }


    public void setThumbnail(int thumbnail) {
        Thumbnail = thumbnail;
    }
}
