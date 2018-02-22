package io.github.zunpiau.web.controller;

import io.github.zunpiau.dao.BaseRepository;
import io.github.zunpiau.web.Response;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.net.URI;

public class BaseController<T extends BaseRepository> {

    private T t;

    public BaseController(T t) {
        this.t = t;
    }

    @RequestMapping(path = "")
    public ResponseEntity getUrl(@RequestParam(required = false) String date) {
        HttpHeaders header = new HttpHeaders();
        if (date == null) {
            header.setLocation(URI.create(t.getLastUrl()));
            return new ResponseEntity(header, HttpStatus.FOUND);
        }
        if (!date.matches("\\d{4}-\\d{2}-\\d{2}")) {
            return new ResponseEntity<>(new Response<>(Response.ResponseCode.BAD_REQUEST, "info format: yyyy-MM-dd"),
                    HttpStatus.OK);
        }
        header.setLocation(URI.create(t.getUrl(date)));
        return new ResponseEntity(header, HttpStatus.FOUND);
    }

    @ResponseBody
    @RequestMapping(path = "/info",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Response getWallpaper(@RequestParam(required = false) String date) {
        if (date == null) {
            return new Response<>(t.getLastWallpaper());
        }
        if (!date.matches("\\d{4}-\\d{2}-\\d{2}")) {
            return new Response<>(Response.ResponseCode.BAD_REQUEST, "info format: yyyy-MM-dd");
        }
        return new Response<>(t.getWallpaper(date));
    }

    @ResponseBody
    @ExceptionHandler(EmptyResultDataAccessException.class)
    public Response emptyResultExceptionHandle() {
        return new Response<>(Response.ResponseCode.BAD_REQUEST, "Not available");
    }

}
