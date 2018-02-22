package io.github.zunpiau.dao;

public abstract class BaseRepository<T> {

    public abstract String getLastUrl();

    public abstract T getLastWallpaper();

    public abstract String getUrl(String date);

    public abstract T getWallpaper(String date);

    public abstract void save(T wallpaper);
}
