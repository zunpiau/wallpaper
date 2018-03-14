package io.github.zunpiau.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/status")
public class StatusController {

    private final JdbcTemplate template;

    @Autowired
    public StatusController(JdbcTemplate template) {
        this.template = template;
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "", method = RequestMethod.HEAD)
    public void status() {
    }

    @RequestMapping(value = "/database", method = RequestMethod.HEAD)
    public ResponseEntity databaseStatus() {
        return (template.queryForObject("SELECT 1", Integer.TYPE) == 1)
                ? new ResponseEntity(HttpStatus.OK)
                : new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
