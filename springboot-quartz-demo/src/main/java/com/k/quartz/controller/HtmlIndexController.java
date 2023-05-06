package com.k.quartz.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author k 2023/4/17 16:55
 */
@Controller
public class HtmlIndexController {
    @GetMapping(value = {"/"})
    public String login() {
        return "login";
    }


    @GetMapping(value = {"/index"})
    public String index() {
        return "index";
    }


}
