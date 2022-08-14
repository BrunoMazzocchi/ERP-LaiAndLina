package com.laiandlina.crm.web.controller;

import com.laiandlina.crm.domain.service.*;
import com.laiandlina.crm.persistance.data.*;
import com.laiandlina.crm.persistance.entity.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.security.access.prepost.*;
import org.springframework.security.authentication.*;
import org.springframework.security.core.*;
import org.springframework.security.core.context.*;
import org.springframework.stereotype.*;
import org.springframework.ui.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.*;

import javax.servlet.http.*;
import java.text.*;

@Controller
class AppController {
    //This controller provides a basic controller to the app for MVC.

    @Autowired
    private UserService userService;

    @Autowired
    private AuthController authController;


    //The following controller will redirect you to the new Login form (AuthController Login)
    //If you are currently logged and try to go back, it will redirect you to main dashboard.
    @GetMapping("/login")
    public String showLoginForm(Model model) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            return "/login";
        }

        return "redirect:/index";
    }


    @RequestMapping(value="/signout", method = RequestMethod.GET)
    public String logoutPage (HttpServletRequest request, HttpServletResponse response) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        User user = userService.getByEmail(userName);

        //LogOutForm
        LogOutRequest logOutRequest = new LogOutRequest();
        //LogOutToken
        logOutRequest.setToken(getToken(request));
        //LogOut device information
        DeviceInfo deviceInfo = new DeviceInfo();
        deviceInfo.setDeviceId("1");
        deviceInfo.setDeviceType("1");
        logOutRequest.setDeviceInfo(deviceInfo);


        //LogOutUserPrincipal
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        authController.logoutUser(userPrincipal, logOutRequest, request, response);
        return "redirect:/login";
    }

    //After a sucessfull login, you'll get current user and send it to the path.
    @RequestMapping(value="/index", method=RequestMethod.GET)
    public ModelAndView dashboard(Model model) throws ParseException {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("index");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        User user = userService.getByEmail(userName);
        modelAndView.addObject(user);


        return modelAndView;
    }



    //Gets token for authorization purposes.
    protected String getToken(HttpServletRequest request) {
        String wood = "";
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("Authorization")) {
                return wood = cookie.getValue();
            }
        }
        return null;
    }




}
