package io.github.zunpiau.dao;

import io.github.zunpiau.domain.Wallpaper;

public abstract class BaseRepository<T extends Wallpaper> {

    public abstract String getLastUrl();

    public abstract T getLastWallpaper();

    public abstract String getUrl(String date);

    public abstract T getWallpaper(String date);

    public abstract void save(T wallpaper);
}
