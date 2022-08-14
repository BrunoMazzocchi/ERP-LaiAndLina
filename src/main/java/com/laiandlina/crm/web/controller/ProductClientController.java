package com.laiandlina.crm.web.controller;

import com.laiandlina.crm.domain.service.*;
import com.laiandlina.crm.persistance.entity.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.security.core.*;
import org.springframework.security.core.context.*;
import org.springframework.ui.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.*;

import javax.servlet.http.*;
import java.text.*;

@RestController
@RequestMapping("/control/order")
public class ProductClientController {

    @Autowired
    private ProductClientService productClientService;
    @Autowired
    private UserService userService;

    //Mapping to list all active orders
    @GetMapping(value = "/active")
    public ModelAndView getActiveOrder(HttpServletRequest request, Authentication authentication) {
        ModelAndView modelAndView = new ModelAndView();

        modelAndView.setViewName("order/orders.html");
        modelAndView.addObject("order", productClientService.findAllActiveOrders());
        authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        User user = userService.getByEmail(userName);
        modelAndView.addObject(user);

        return modelAndView;
    }

    //Mapping to list all completed orders.
    @GetMapping(value = "/completed")
    public ModelAndView getCompletedOrder(HttpServletRequest request, Authentication authentication) {
        ModelAndView modelAndView = new ModelAndView();

        modelAndView.setViewName("order/ordersCompleted.html");
        modelAndView.addObject("order", productClientService.findAllCompletedOrders());
        authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        User user = userService.getByEmail(userName);
        modelAndView.addObject(user);

        return modelAndView;
    }

    //Mapping to create a new order form.
/*    @GetMapping(value = "/new")
    public ModelAndView newOrder(Model model, Authentication authentication) throws ParseException {
        ModelAndView modelAndView = new ModelAndView();
        ProductClient productClient = new ProductClient();

        authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        User user = userService.getByEmail(userName);
        System.out.println(userName);
        modelAndView.addObject(user);

        modelAndView.addObject("client", clientRestController.getAllClients());
        modelAndView.addObject("product", productRestController.getAllProducts());
        model.addAttribute("newOrder", new ProductClient());

        modelAndView.setViewName("order/newOrder.html");

        return modelAndView;
    }*/


}
