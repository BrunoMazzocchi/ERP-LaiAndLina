package com.laiandlina.crm.web.controller;

import com.laiandlina.crm.domain.service.*;
import com.laiandlina.crm.persistance.data.*;
import com.laiandlina.crm.persistance.entity.*;
import com.laiandlina.crm.persistance.repository.*;
import org.hibernate.procedure.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.security.core.*;
import org.springframework.security.core.context.*;
import org.springframework.ui.*;
import org.springframework.validation.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.*;

import javax.servlet.http.*;
import java.sql.*;
import java.sql.Date;
import java.text.*;
import java.util.*;

@RestController
@RequestMapping("/control/order")
public class ProductClientController {

    @Autowired
    private ProductClientService productClientService;
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ClientService clientService;
    @Autowired
    private ProductService productService;
    @Autowired
    private ProductClientRepository productClientRepository;
    @Autowired
    private EmailService javaMailSender;
    @Autowired
    private NoteRepository noteRepository;


    //Mapping to list all active orders
    @GetMapping(value = "/active")
    public ModelAndView getActiveOrder(HttpServletRequest request, Authentication authentication) {
        ModelAndView modelAndView = new ModelAndView();

        modelAndView.setViewName("order/orders.html");
        modelAndView.addObject("order", productClientService.findAllActiveOrders());
        System.out.println(productClientService.findAllActiveOrders());
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
    @GetMapping(value = "/newOrder")
    public ModelAndView newOrder(Model model, Authentication authentication) throws ParseException {
        ModelAndView modelAndView = new ModelAndView();
        ProductClient productClient = new ProductClient();

        authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        User user = userService.getByEmail(userName);
        modelAndView.addObject(user);

        modelAndView.addObject("client", clientService.findAll());
        modelAndView.addObject("product", productService.findAll());
        model.addAttribute("newOrder", new ProductClient());

        modelAndView.setViewName("order/newOrder.html");

        return modelAndView;
    }

    //This function will create a new order and notify users (ADMIN and userCreator)
    @RequestMapping(value = "/saveOrderForm", method = RequestMethod.POST)
    public ModelAndView createOrder(@ModelAttribute("newOrder") NewProductClientForm formOrder,
                                  BindingResult bindingResult,
                                  ModelMap model) {

        if(bindingResult.hasErrors()){
            model.addAttribute("index.html");
        }
        ProductClient productClient = new ProductClient();
        productClient.setIdProduct(formOrder.getIdProduct());
        productClient.setIdClient(formOrder.getIdClient());
        productClient.setEndDate(formOrder.getEndDate());
        productClient.setState(formOrder.getState());

        Date timestamp = new Date(System.currentTimeMillis());
        productClient.setStartDate(timestamp);
        productClient.setFinalPrice(formOrder.getFinalPrice());

        //Assign the product client to userFollowingProductClient. This will be to follow-up email
        //when assigned productClients are modified.

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        User user = userService.getByEmail(userName);

        Set<String> strUsers = Collections.singleton(formOrder.getUser());


        Set<User> users = new HashSet<>();
        List<User> usersByRole = userRepository.findByRoles(1);

        usersByRole.forEach(userInRole -> {
            User userForFollowUp = userInRole;
            users.add(userForFollowUp);
        });

        users.add(user);
        productClient.setUsers(users);

        ProductClient newProductClient = productClientService.save(productClient);


        users.forEach(userForEmail -> {
            javaMailSender.sendEmail(userForEmail.getEmail(), "Se ha generado una nueva orden",
                    "Se ha generado una" +
                    " orden nueva. Numero de orden: '" + newProductClient.getId() + ", Entrega programada para: " +
                    newProductClient.getEndDate() + "' " +
                    ". Puedes continuar aqui http://localhost:8080/control/order/active");
        });
        return new ModelAndView("redirect:/control/order/active", model);
    }


    @RequestMapping(value="/order={orderId}", method=RequestMethod.GET)
    public ModelAndView getOrder(@PathVariable("orderId") int orderId, Authentication authentication) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("order/order.html");
        modelAndView.addObject("productClient", productClientService.findById(orderId));
        modelAndView.addObject("notes", noteRepository.findNoteByProductClient(orderId));
        modelAndView.addObject("order", productClientRepository.findOrderById(orderId));
        authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        User user = userService.getByEmail(userName);
        System.out.println(userName);
        modelAndView.addObject(user);
        return modelAndView;
    }

    @RequestMapping(value = "/editOrderForm", method = RequestMethod.POST)
    public ModelAndView editOrder(@ModelAttribute("currentOrder") NewProductClientForm formOrder,
                                  BindingResult bindingResult,
                                  ModelMap model) {

        if(bindingResult.hasErrors()){
            model.addAttribute("index.html");
        }
        ProductClient productClient = new ProductClient();
        productClient.setId(formOrder.getId());
        productClient.setIdProduct(formOrder.getIdProduct());
        productClient.setIdClient(formOrder.getIdClient());
        productClient.setEndDate(formOrder.getEndDate());
        productClient.setState(formOrder.getState());
        productClient.setStartDate(formOrder.getStartDate());
        productClient.setFinalPrice(formOrder.getFinalPrice());
        productClient.setState(2);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        User user = userService.getByEmail(userName);

        Set<String> strUsers = Collections.singleton(formOrder.getUser());


        Set<User> users = new HashSet<>();
        List<User> usersByRole = userRepository.findByRoles(1);

        usersByRole.forEach(userInRole -> {
            User userForFollowUp = userInRole;
            users.add(userForFollowUp);
        });

        users.add(user);
        productClient.setUsers(users);

        ProductClient editedProductClient = productClientService.save(productClient);

        users.forEach(userForEmail -> {
            javaMailSender.sendEmail(userForEmail.getEmail(), "Se ha editado una orden",
                    "Se ha editado una" +
                            " orden existente. Numero de orden: " + editedProductClient.getId() + ", Puedes ver mas " +
                            "informacion aqui http://localhost:8080/control/order/order="+editedProductClient.getId()+"");
        });
        return new ModelAndView("redirect:/control/order/active", model);
    }
}
