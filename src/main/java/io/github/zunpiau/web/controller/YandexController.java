package io.github.zunpiau.web.controller;

import io.github.zunpiau.dao.YandexRepository;
import io.github.zunpiau.domain.YandexWallpaper;
import io.github.zunpiau.web.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping(path = "/yandex")
public class YandexController {

    private YandexRepository repository;

    @Autowired
    public YandexController(YandexRepository repository) {
        this.repository = repository;
    }

    @RequestMapping(path = "")
    public RedirectView daily() {
        return new RedirectView(repository.getLast());
    }

    @RequestMapping(path = "/{date}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @Validated
    public Response date(@PathVariable String date) {
        if (!date.matches("\\d{4}-\\d{2}-\\d{2}")) {
            return new Response<>(Response.ResponseCode.BAD_REQUEST, "date format: yyyy-MM-dd");
        }
        YandexWallpaper wallpaper = repository.get(date);
        if (wallpaper == null) {
            return new Response<>(Response.ResponseCode.BAD_REQUEST, "Not available");
        }
        return new Response<>(wallpaper);

    }

}
