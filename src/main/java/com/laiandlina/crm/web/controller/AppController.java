package com.laiandlina.crm.web.controller;

import com.laiandlina.crm.domain.service.*;
import com.laiandlina.crm.persistance.data.*;
import com.laiandlina.crm.persistance.entity.*;
import com.laiandlina.crm.persistance.repository.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.security.access.prepost.*;
import org.springframework.security.core.*;
import org.springframework.security.core.context.*;
import org.springframework.stereotype.*;
import org.springframework.ui.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.*;

import java.text.*;

@Controller
class AppController {
    //This controller provides a basic controller to the app for MVC.

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;
    @Autowired
    private DepartmentService departmentService;


    //The following controller will redirect you to the new Login form (AuthController Login)
    @RequestMapping(value="/login", method=RequestMethod.GET)
    public ModelAndView login(Model model) throws ParseException {
        ModelAndView modelAndView = new ModelAndView();

        model.addAttribute("login", new LoginForm());
        modelAndView.setViewName("login.html");


        return modelAndView;
    }

    //After a sucessfull login, you'll get current user and send it to the path.
    @RequestMapping(value="/index", method=RequestMethod.GET)
    public ModelAndView dashboard(Model model) throws ParseException {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("index");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        User user = userService.getByEmail(userName);
        System.out.println(userName);
        modelAndView.addObject(user);


        return modelAndView;
    }

    //The following controller will redirect you to the new User form (AuthController Sign up)
    @RequestMapping(value="/newUser", method=RequestMethod.GET)
    @PreAuthorize("hasRole('ADMIN')")
    public ModelAndView newOrder(Model model) throws ParseException {
        ModelAndView modelAndView = new ModelAndView();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        User user = userService.getByEmail(userName);
        modelAndView.addObject(user);

        modelAndView.addObject("roles", roleService.findAll());
        modelAndView.addObject("departments", departmentService.findAll());
        model.addAttribute("newUser", new User());

        modelAndView.setViewName("user/newUser.html");

        return modelAndView;
    }

}
