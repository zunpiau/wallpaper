package io.github.zunpiau.domain;

import java.io.Serializable;

public class YandexWallpaper implements Wallpaper, Serializable {

    private String date;
    private String url;
    private String title;
    private String description;
    private String authorName;
    private String authorLink;
    private String partner;
    private String hashDate;

    public YandexWallpaper() {
    }

    public YandexWallpaper(String date,
            String url,
            String title,
            String description,
            String authorName,
            String authorLink,
            String partner,
            String hashDate) {
        this.date = date;
        this.url = url;
        this.title = title;
        this.description = description;
        this.authorName = authorName;
        this.authorLink = authorLink;
        this.partner = partner;
        this.hashDate = hashDate;
    }

    @Override
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getAuthorLink() {
        return authorLink;
    }

    public void setAuthorLink(String authorLink) {
        this.authorLink = authorLink;
    }

    public String getPartner() {
        return partner;
    }

    public void setPartner(String partner) {
        this.partner = partner;
    }

    public String getHashDate() {
        return hashDate;
    }

    public void setHashDate(String hashDate) {
        this.hashDate = hashDate;
    }
}
