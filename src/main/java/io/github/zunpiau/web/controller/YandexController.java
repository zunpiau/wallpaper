package io.github.zunpiau.web.controller;

import io.github.zunpiau.dao.YandexRepository;
import io.github.zunpiau.web.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.net.URI;

@Controller
@RequestMapping(path = "/yandex")
public class YandexController {

    private YandexRepository repository;

    @Autowired
    public YandexController(YandexRepository repository) {
        this.repository = repository;
    }

    @RequestMapping(path = "")
    public ResponseEntity getUrl(@RequestParam(required = false) String date) {
        HttpHeaders header = new HttpHeaders();
        if (date == null) {
            header.setLocation(URI.create(repository.getLastUrl()));
            return new ResponseEntity(header, HttpStatus.FOUND);
        }
        if (!date.matches("\\d{4}-\\d{2}-\\d{2}")) {
            return new ResponseEntity<>(new Response<>(Response.ResponseCode.BAD_REQUEST, "info format: yyyy-MM-dd"),
                    HttpStatus.OK);
        }
        header.setLocation(URI.create(repository.getUrl(date)));
        return new ResponseEntity(header, HttpStatus.FOUND);
    }

    @ResponseBody
    @RequestMapping(path = "/info",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Response getWallpaper(@RequestParam(required = false) String date) {
        if (date == null) {
            return new Response<>(repository.getLastWallpaper());
        }
        if (!date.matches("\\d{4}-\\d{2}-\\d{2}")) {
            return new Response<>(Response.ResponseCode.BAD_REQUEST, "info format: yyyy-MM-dd");
        }
        return new Response<>(repository.getWallpaper(date));
    }

    @ResponseBody
    @ExceptionHandler(EmptyResultDataAccessException.class)
    public Response emptyResultExceptionHandle() {
        return new Response<>(Response.ResponseCode.BAD_REQUEST, "Not available");
    }

}
