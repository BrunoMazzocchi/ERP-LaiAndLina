package com.laiandlina.erp.web.controller;

import org.springframework.boot.web.servlet.error.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.*;

import javax.servlet.http.*;

@RestController
public class ErrorHandlerController implements ErrorController {

    @RequestMapping("/error")
    public ModelAndView handleError(HttpServletResponse response)
    {
        ModelAndView modelAndView = new ModelAndView();

        switch (response.getStatus()) {
            case 404:
                modelAndView.setViewName("errorHandle/404");
                break;
            case 403:
                modelAndView.setViewName("errorHandle/403");
                break;
            case 401:
                modelAndView.setViewName("errorHandle/401");
                break;
            default:
                modelAndView.setViewName("errorHandle/500");

    }


        return modelAndView;
    }

    public String getErrorPath() {
        return "/error";
    }

}
