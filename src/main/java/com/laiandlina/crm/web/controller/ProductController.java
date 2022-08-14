package com.laiandlina.crm.web.controller;


import com.laiandlina.crm.domain.service.*;
import com.laiandlina.crm.persistance.entity.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.security.core.*;
import org.springframework.security.core.context.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.*;

import javax.servlet.http.*;

@RestController
@RequestMapping("/control/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;

    //Mapping to list all products
    @GetMapping(value = "/all")
    public ModelAndView getAllProducts(HttpServletRequest request, Authentication authentication) {
        ModelAndView modelAndView = new ModelAndView();

        modelAndView.setViewName("production/products.html");
        modelAndView.addObject("products", productService.findAll());
        authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        User user = userService.getByEmail(userName);
        modelAndView.addObject(user);
        return modelAndView;
    }

}
