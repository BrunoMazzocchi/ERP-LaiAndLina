package com.laiandlina.erp.web.controller;

import com.laiandlina.erp.domain.service.*;
import com.laiandlina.erp.persistance.data.*;
import com.laiandlina.erp.persistance.entity.*;
import com.laiandlina.erp.persistance.repository.*;
import com.laiandlina.erp.web.controller.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.security.access.prepost.*;
import org.springframework.security.authentication.*;
import org.springframework.security.core.*;
import org.springframework.security.core.annotation.*;
import org.springframework.security.core.context.*;
import org.springframework.stereotype.*;
import org.springframework.ui.*;
import org.springframework.util.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.*;

import javax.servlet.http.*;
import java.sql.Date;
import java.text.*;
import java.time.*;
import java.text.*;
import java.util.*;

@Controller
class AppController {
    //This controller provides a basic controller to the app for MVC.

    @Autowired
    private UserService userService;
    @Autowired
    private AuthController authController;
    @Autowired
    private ProductClientRepository productClientRepository;
    //The following controller will redirect you to the new Login form (AuthController Login)
    //If you are currently logged and try to go back, it will redirect you to main dashboard.
    @GetMapping("/login")
    public String showLoginForm() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            return "/login";
        }

        return "redirect:/index";
    }


    @RequestMapping(value="/signout", method = RequestMethod.GET)
    public String logoutPage (HttpServletRequest request, HttpServletResponse response,
                              @AuthenticationPrincipal UserPrincipal userPrincipal) {
        //LogOutForm
        LogOutRequest logOutRequest = new LogOutRequest();
        //LogOutToken
        logOutRequest.setToken(getToken(request));
        //LogOut device information
        DeviceInfo deviceInfo = new DeviceInfo();

        deviceInfo.setDeviceId(getRemoteAddr(request).toString());
        String browserType = request.getHeader("User-Agent");
        deviceInfo.setDeviceType(browserType);
        logOutRequest.setDeviceInfo(deviceInfo);
        //LogOutUserPrincipal
        authController.logoutUser(userPrincipal, logOutRequest, request, response);
        return "redirect:/login";
    }

    //After a sucessfull login, you'll get current user and send it to the path.
    @RequestMapping(value="/index", method=RequestMethod.GET)
    public ModelAndView dashboard(@AuthenticationPrincipal UserPrincipal userPrincipal) throws ParseException {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("index");
        modelAndView.addObject(userPrincipal);
        LocalDate currentdate = LocalDate.now();
        String currentStartMonth = currentdate.getYear() + "-" + currentdate.getMonthValue() + "-01";
        List<Integer> orders =new ArrayList<Integer>();
        orders.add(productClientRepository.getOrderCompletedCount(currentStartMonth));
        orders.add(productClientRepository.getOrderActiveCount(currentStartMonth));

        modelAndView.addObject("orders", orders);

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



    @RequestMapping(value="", method=RequestMethod.GET)
    public String redirect() throws ParseException {
        return "redirect:/index";

    }

    private String getRemoteAddr(HttpServletRequest req) {
        if (!ObjectUtils.isEmpty(req.getHeader("X-Real-IP"))) {
            return req.getHeader("X-Real-IP");
        }
        return req.getRemoteAddr();
    }
}
