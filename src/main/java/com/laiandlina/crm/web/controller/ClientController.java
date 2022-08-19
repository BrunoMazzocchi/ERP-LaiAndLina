package com.laiandlina.crm.web.controller;

import com.laiandlina.crm.domain.service.*;
import com.laiandlina.crm.persistance.data.*;
import com.laiandlina.crm.persistance.entity.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.security.core.*;
import org.springframework.security.core.context.*;
import org.springframework.ui.*;
import org.springframework.validation.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.*;

import javax.servlet.http.*;
import java.text.*;
import java.util.*;

@RestController
@RequestMapping("/control/client")
public class ClientController {

    @Autowired
    private ClientService clientService;

    @Autowired
    private UserService userService;

    //Mapping to list all client
    @GetMapping(value = "/all")
    public ModelAndView getAllClient(HttpServletRequest request, Authentication authentication) {
        ModelAndView modelAndView = new ModelAndView();

        modelAndView.setViewName("production/clients.html");
        modelAndView.addObject("clients", clientService.findAll());
        authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        User user = userService.getByEmail(userName);
        modelAndView.addObject(user);
        return modelAndView;
    }

    @RequestMapping(value="/newClient", method=RequestMethod.GET)
    public ModelAndView newClient(Model model, Authentication authentication) throws ParseException {
        ModelAndView modelAndView = new ModelAndView();
        model.addAttribute("newClient", new Client());
        modelAndView.setViewName("production/newClient.html");
        authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        User user = userService.getByEmail(userName);
        modelAndView.addObject(user);
        return modelAndView;
    }

    @RequestMapping(value="/client={clientId}", method=RequestMethod.GET)
    public ModelAndView editClient(Model model, Authentication authentication,
                                   @PathVariable("clientId") int clientId)throws ParseException {

        Client client = clientService.findClientById(clientId).stream().findFirst().orElse(null);

        ModelAndView modelAndView = new ModelAndView();
        model.addAttribute("client", client);
        modelAndView.setViewName("production/client.html");
        authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        User user = userService.getByEmail(userName);
        modelAndView.addObject(user);



        return modelAndView;
    }

    @PostMapping("/editClientForm")
    public ModelAndView editClientForm(@ModelAttribute("currentClient") Client client,
                                  BindingResult bindingResult,
                                  ModelMap model) {

        if(bindingResult.hasErrors()){
            model.addAttribute("index.html");
        }
        clientService.save(client);


        return new ModelAndView("redirect:/control/client/all", model);
    }


    @PostMapping("/saveClientForm")
    public ModelAndView save(@ModelAttribute("newClient") NewClientForm clientForm,
                             BindingResult bindingResult,
                             ModelMap model){



        Client client = new Client();
        client.setFirstName(clientForm.getFirstName());
        client.setLastName(clientForm.getLastName());
        client.setIdentification(clientForm.getIdentification());

        clientService.save(client);
        return new ModelAndView("redirect:/control/client/all", model);
    }

}
