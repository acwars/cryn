package com.onlinejudge.cryn.rest.backend;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller("backendIndexController")
@RequestMapping("/backend")
public class IndexController {


    @RequestMapping("/")
    public String index(HttpServletRequest request) {
       request.setAttribute("backendIndexActive", true);
       return "backend/index";

    }


}
