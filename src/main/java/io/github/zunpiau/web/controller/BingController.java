package io.github.zunpiau.web.controller;

import io.github.zunpiau.dao.BingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/bing")
public class BingController extends BaseController<BingRepository> {

    @Autowired
    public BingController(BingRepository repository) {
        super(repository);
    }
}
