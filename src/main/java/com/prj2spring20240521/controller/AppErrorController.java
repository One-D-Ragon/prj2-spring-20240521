package com.prj2spring20240521.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class AppErrorController implements ErrorController {
    
    // error가 발생했을때 react가 다시 일하게 함
    @RequestMapping("/error")
    public String handleError() {
        return "/";
    }
}
