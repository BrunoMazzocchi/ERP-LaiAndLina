package com.laiandlina.erp.web.controller;

import com.laiandlina.erp.domain.service.*;
import com.laiandlina.erp.persistance.data.*;
import com.laiandlina.erp.persistance.entity.*;
import com.laiandlina.erp.persistance.repository.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.security.core.annotation.*;
import org.springframework.ui.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.*;

import javax.servlet.http.*;
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
    public ModelAndView getActiveOrder(HttpServletRequest request,
                                       @AuthenticationPrincipal UserPrincipal userPrincipal) {
        try {
            ModelAndView modelAndView = new ModelAndView();
            modelAndView.setViewName("order/orders.html");
            modelAndView.addObject("order", productClientService.findAllActiveOrders());
            modelAndView.addObject(userPrincipal);
            return modelAndView;
        } catch (Exception error){
            System.out.println("Error on redirect to show active orders: " + error);
            return new ModelAndView("redirect:/index");
        }
    }

    //Mapping to list all completed orders.
    @GetMapping(value = "/completed")
    public ModelAndView getCompletedOrder(HttpServletRequest request,
                                          @AuthenticationPrincipal UserPrincipal userPrincipal) {
        try{
            ModelAndView modelAndView = new ModelAndView();

            modelAndView.setViewName("order/ordersCompleted.html");
            modelAndView.addObject("order", productClientService.findAllCompletedOrders());
            modelAndView.addObject(userPrincipal);
            return modelAndView;
        } catch (Exception error){
            System.out.println("Error on redirect to show completed orders: " + error);
            return new ModelAndView("redirect:/index");
        }
    }

    //Mapping to create a new order form.
    @GetMapping(value = "/newOrder")
    public ModelAndView newOrder(Model model,
                                 @AuthenticationPrincipal UserPrincipal userPrincipal) throws ParseException {
        try{
            ModelAndView modelAndView = new ModelAndView();
            ProductClient productClient = new ProductClient();
            modelAndView.addObject(userPrincipal);
            modelAndView.addObject("client", clientService.findAll());
            modelAndView.addObject("product", productService.findAll());
            model.addAttribute("newOrder", new ProductClient());

            modelAndView.setViewName("order/newOrder.html");

            return modelAndView;
        } catch (Exception error){
            System.out.println("Error on redirect to create order: " + error);
            return new ModelAndView("redirect:/control/order/active");
        }
    }

    //This function will create a new order and notify users (ADMIN and userCreator)
    @RequestMapping(value = "/saveOrderForm", method = RequestMethod.POST)
    public ModelAndView createOrder(@ModelAttribute("newOrder") NewProductClientForm formOrder,
                                    @AuthenticationPrincipal UserPrincipal userPrincipal,
                                    ModelMap model) {
        try{
            ProductClient productClient = new ProductClient();
            productClient.setIdProduct(formOrder.getIdProduct());
            productClient.setIdClient(formOrder.getIdClient());
            productClient.setEndDate(formOrder.getEndDate());
            productClient.setState(formOrder.getState());
            productClient.setBasePrice(productService.findById(
                    formOrder.getIdProduct()).stream().findFirst()
                    .orElse(null).getPrice());
            Date timestamp = new Date(System.currentTimeMillis());
            productClient.setStartDate(timestamp);
            productClient.setFinalPrice(formOrder.getFinalPrice());
            //Assign the product client to userFollowingProductClient. This will be to follow-up email
            //when assigned productClients are modified.

            Set<String> strUsers = Collections.singleton(formOrder.getUser());
            User user = userService.getByEmail(userPrincipal.getEmail());
            HashSet<User> users = new HashSet<>();
            List<User> usersByRole = userRepository.findByRoles(1);

            usersByRole.forEach(userInRole -> {
                User userForFollowUp = userInRole;
                users.add(userForFollowUp);
            });

            users.add(user);
            productClient.setUsers(users);

            ProductClient newProductClient = productClientService.save(productClient);
            String subject = "Se ha generado una nueva orden";
            String body = "Se ha generado una" +
                    " orden nueva. Numero de orden: '" + newProductClient.getId() + ", Entrega programada para: " +
                    newProductClient.getEndDate() + "' " +
                    ". Puedes ver mas informacion aqui: http://localhost:8000/control/order/order="+newProductClient.getId();

            javaMailSender.sendEmailToUser(subject, body, users);

            return new ModelAndView("redirect:/control/order/active?msg=1", model);
        } catch (Exception error){
            System.out.println("Error on create order: " + error);
            return new ModelAndView("redirect:/control/order/active?msg=2", model);
        }
    }


    @RequestMapping(value="/order={orderId}", method=RequestMethod.GET)
    public ModelAndView getOrder(@PathVariable("orderId") int orderId,
                                 @AuthenticationPrincipal UserPrincipal userPrincipal) {
        try {
            ModelAndView modelAndView = new ModelAndView();
            modelAndView.setViewName("order/order.html");
            modelAndView.addObject("productClient", productClientService.findById(orderId));
            modelAndView.addObject("notes", noteRepository.findNoteByProductClient(orderId));
            modelAndView.addObject("order", productClientRepository.findOrderById(orderId));
            modelAndView.addObject(userPrincipal);
            return modelAndView;
        } catch (Exception error){
            System.out.println("Error on redirect to order: " + error);
            return new ModelAndView("redirect:/control/order/active");
        }
    }

    @RequestMapping(value="/orderCompleted={orderId}", method=RequestMethod.GET)
    public ModelAndView getOrderCompleted(@PathVariable("orderId") int orderId,
                                 @AuthenticationPrincipal UserPrincipal userPrincipal) {
        try {
            ModelAndView modelAndView = new ModelAndView();
            modelAndView.setViewName("order/order.html");
            modelAndView.addObject("productClient", productClientService.findById(orderId));
            modelAndView.addObject("notes", noteRepository.findNoteByProductClient(orderId));
            modelAndView.addObject("order", productClientRepository.findOrderByIdActive(orderId));
            modelAndView.addObject(userPrincipal);
            return modelAndView;
        } catch (Exception error){
            System.out.println("Error on redirect to order: " + error);
            return new ModelAndView("redirect:/control/order/active");
        }
    }


    @RequestMapping(value = "/editOrderForm", method = RequestMethod.POST)
    public ModelAndView editOrder(@ModelAttribute("currentOrder") NewProductClientForm formOrder,
                                  ModelMap model,  @AuthenticationPrincipal UserPrincipal userPrincipal) {
        try{
            ProductClient productClient = new ProductClient();
            productClient.setId(formOrder.getId());
            productClient.setIdProduct(formOrder.getIdProduct());
            productClient.setIdClient(formOrder.getIdClient());
            productClient.setEndDate(formOrder.getEndDate());
            productClient.setState(formOrder.getState());
            productClient.setStartDate(formOrder.getStartDate());
            productClient.setFinalPrice(formOrder.getFinalPrice());
            productClient.setState(2);
            User user = userService.getByEmail(userPrincipal.getEmail());
            HashSet<User> users = new HashSet<>();
            var usersByRole = userRepository.findByRoles(1);

            usersByRole.forEach(userInRole -> {
                User userForFollowUp = userInRole;
                users.add(userForFollowUp);
            });

            users.add(user);
            productClient.setUsers(users);

            ProductClient editedProductClient = productClientService.save(productClient);

            String subject = "Se ha editado una orden";
            String body = "Se ha editado una" +
                    " orden existente. Numero de orden: " + editedProductClient.getId() + ", Puedes ver mas " +
                    "informacion aqui http://localhost:8000/control/order/order="+editedProductClient.getId()+"";

            javaMailSender.sendEmailToUser(subject, body, users);
            return new ModelAndView("redirect:/control/order/active?msg=3", model);
        } catch (Exception error){
            System.out.println("Error on edit order: " + error);
            return new ModelAndView("redirect:/control/order/active?msg=4", model);
        }
    }

    @PostMapping("/deleteProductClient={idPC}")
    public ModelAndView deleteProductClient(@PathVariable("idPC") int idPC){
        try{
            ProductClient productClient = productClientService.findById(idPC);
            productClient.setState(5);
            productClientService.save(productClient);
            return new ModelAndView("redirect:/control/order/active?msg=5");
        } catch (Exception error){
            System.out.println("Error on order delete: " + error);
            return new ModelAndView("redirect:/control/order/active?msg=6");
        }
    }
}
