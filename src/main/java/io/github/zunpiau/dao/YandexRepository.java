package io.github.zunpiau.dao;

import io.github.zunpiau.domain.YandexWallpaper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class YandexRepository {

    private final String ALL_FILED = " date, url, title, description, author_name, author_link, partner, hash_date ";
    private final String SQL_SELECT_ALL = "SELECT " + ALL_FILED + " FROM yandex ";
    private final String SQL_SELECT_URL = "SELECT url FROM yandex ";
    private final String SQL_ORDER = " ORDER BY id DESC LIMIT 1";
    private final String SQL_WHERE = " WHERE date = ?";

    @Autowired
    private JdbcTemplate template;
    private Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    public String getLastUrl() {
        return template.queryForObject(SQL_SELECT_URL + SQL_ORDER, String.class);
    }

    public YandexWallpaper getLastWallpaper() {
        return template.queryForObject(SQL_SELECT_ALL + SQL_ORDER, new YandexRowMapper());
    }

    public String getUrl(String date) {
        return template.queryForObject(SQL_SELECT_URL + SQL_WHERE, String.class, date);
    }

    public YandexWallpaper getWallpaper(String date) {
        return template.queryForObject(SQL_SELECT_ALL + SQL_WHERE, new YandexRowMapper(), date);
    }

    public void save(YandexWallpaper wallpaper) {
        logger.debug(wallpaper.getDate());
        template.update("INSERT INTO yandex( " + ALL_FILED + " ) VALUES(?, ?, ?, ?, ?, ?, ?, ?)",
                wallpaper.getDate(),
                wallpaper.getUrl(),
                wallpaper.getTitle(),
                wallpaper.getDescription(),
                wallpaper.getAuthorName(),
                wallpaper.getAuthorLink(),
                wallpaper.getPartner(),
                wallpaper.getHashDate());
    }

    static class YandexRowMapper implements RowMapper<YandexWallpaper> {

        @Override
        public YandexWallpaper mapRow(ResultSet resultSet, int rowNum) throws SQLException {
            return new YandexWallpaper(
                    resultSet.getString("date"),
                    resultSet.getString("url"),
                    resultSet.getString("title"),
                    resultSet.getString("description"),
                    resultSet.getString("author_name"),
                    resultSet.getString("author_link"),
                    resultSet.getString("partner"),
                    resultSet.getString("hash_date")
            );
        }
    }

}
