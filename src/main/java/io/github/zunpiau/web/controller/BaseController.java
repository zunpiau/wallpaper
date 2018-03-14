package io.github.zunpiau.web.controller;

import io.github.zunpiau.dao.BaseRepository;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.net.URI;

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
    public ResponseEntity getWallpaper(@RequestParam(required = false) String date) {
        if (date == null) {
            return ResponseEntity.ok()
                    .body(t.getLastWallpaper());
        }
        if (!date.matches("\\d{4}-\\d{2}-\\d{2}")) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok()
                .body(t.getWallpaper(date));
    }

    @ExceptionHandler(EmptyResultDataAccessException.class)
    public ResponseEntity emptyResultExceptionHandle() {
        return ResponseEntity.notFound().build();
    }

}
