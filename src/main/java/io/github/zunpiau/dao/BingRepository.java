package io.github.zunpiau.dao;

import io.github.zunpiau.domain.Bingwallpaper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class BingRepository extends BaseRepository<Bingwallpaper> {

    private final static String NAME_AND_KEY_URL = "bing_lastUrl";
    private final static String NAME_AND_KEY_WALLPAPER = "bing_lastWallpaper";
    private final String ALL_FILED = " date, url, title, copyright ";
    private final String SQL_SELECT_ALL = "SELECT " + ALL_FILED + " FROM bing ";
    private final String SQL_SELECT_URL = "SELECT url FROM bing ";
    private final String SQL_ORDER = " ORDER BY id DESC LIMIT 1";
    private final String SQL_WHERE = " WHERE date = ?";
    private final JdbcTemplate template;
    private final CacheManager manager;
    private Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    @Autowired
    public BingRepository(JdbcTemplate template, CacheManager manager) {
        this.template = template;
        this.manager = manager;
    }

    @Override
    @Cacheable(value = NAME_AND_KEY_URL, key = "'" + NAME_AND_KEY_URL + "'")
    public String getLastUrl() {
        return template.queryForObject(SQL_SELECT_URL + SQL_ORDER, String.class);
    }

    @Override
    @Cacheable(value = NAME_AND_KEY_WALLPAPER, key = "'" + NAME_AND_KEY_WALLPAPER + "'")
    public Bingwallpaper getLastWallpaper() {
        return template.queryForObject(SQL_SELECT_ALL + SQL_ORDER, new BingRowMapper());
    }

    @Override
    public String getUrl(String date) {
        return template.queryForObject(SQL_SELECT_URL + SQL_WHERE, String.class, date);
    }

    @Override
    public Bingwallpaper getWallpaper(String date) {
        return template.queryForObject(SQL_SELECT_ALL + SQL_WHERE, new BingRowMapper(), date);
    }

    @Override
    public void save(Bingwallpaper wallpaper) {
        logger.debug(wallpaper.getDate());
        template.update("INSERT INTO bing( " + ALL_FILED + " ) VALUES(?, ?, ?, ?)",
                wallpaper.getDate(),
                wallpaper.getUrl(),
                wallpaper.getTitle(),
                wallpaper.getCopyright());
        manager.getCache(NAME_AND_KEY_URL).put(NAME_AND_KEY_URL, wallpaper.getUrl());
        manager.getCache(NAME_AND_KEY_WALLPAPER).put(NAME_AND_KEY_WALLPAPER, wallpaper);
    }

    static class BingRowMapper implements RowMapper<Bingwallpaper> {

        @Override
        public Bingwallpaper mapRow(ResultSet resultSet, int rowNum) throws SQLException {
            return new Bingwallpaper(
                    resultSet.getString("date"),
                    resultSet.getString("url"),
                    resultSet.getString("title"),
                    resultSet.getString("copyright")
            );
        }
    }

}
