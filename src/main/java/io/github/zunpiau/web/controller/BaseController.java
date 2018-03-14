package io.github.zunpiau.web.controller;

import io.github.zunpiau.dao.BaseRepository;
import io.github.zunpiau.domain.Wallpaper;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.WebRequest;

import java.net.URI;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;

public class BaseController<T extends BaseRepository> {

    private T t;

    public BaseController(T t) {
        this.t = t;
    }

    @GetMapping(path = "")
    public ResponseEntity getUrl(@RequestParam(required = false) String date) {
        if (date == null) {
            return ResponseEntity.status(HttpStatus.FOUND)
                    .location(URI.create(t.getLastUrl()))
                    .build();
        }
        if (!date.matches("\\d{4}-\\d{2}-\\d{2}")) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create(t.getUrl(date)))
                .build();
    }

    @GetMapping(path = "/info",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getWallpaper(WebRequest request, @RequestParam(required = false) String date) {
        if (date == null) {
            Wallpaper wallpaper = t.getLastWallpaper();
            if (request.checkNotModified(wallpaper.getDate())) {
                return ResponseEntity.status(HttpStatus.NOT_MODIFIED)
                        .build();
            }
            return ResponseEntity.ok()
                    .cacheControl(CacheControl.maxAge(getTodayReminSeconds(), TimeUnit.SECONDS))
                    .eTag(wallpaper.getDate())
                    .body(t.getLastWallpaper());
        }
        if (!date.matches("\\d{4}-\\d{2}-\\d{2}")) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok()
                .body(t.getWallpaper(date));
    }

    private long getTodayReminSeconds() {
        return LocalDateTime.now()
                .until(LocalDate.now().plusDays(1).atStartOfDay(), ChronoUnit.SECONDS);
    }

    @ExceptionHandler(EmptyResultDataAccessException.class)
    public ResponseEntity emptyResultExceptionHandle() {
        return ResponseEntity.notFound().build();
    }

}
