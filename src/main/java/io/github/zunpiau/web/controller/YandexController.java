package io.github.zunpiau.web.controller;

import io.github.zunpiau.dao.YandexRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(path = "/yandex")
public class YandexController extends BaseController<YandexRepository> {

    @Autowired
    public YandexController(YandexRepository repository) {
        super(repository);
    }
}
