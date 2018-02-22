package io.github.zunpiau.domain;

public class Bingwallpaper {

    private final String date;
    private final String url;
    private final String title;
    private final String copyright;

    public Bingwallpaper(String date, String url, String title, String copyright) {
        this.date = date;
        this.url = url;
        this.title = title;
        this.copyright = copyright;
    }

    public String getDate() {
        return date;
    }

    public String getUrl() {
        return url;
    }

    public String getTitle() {
        return title;
    }

    public String getCopyright() {
        return copyright;
    }

    @Override
    public String toString() {
        return "Bingwallpaper{" +
               "date='" + date + '\'' +
               ", url='" + url + '\'' +
               ", title='" + title + '\'' +
               ", copyright='" + copyright + '\'' +
               '}';
    }
}
