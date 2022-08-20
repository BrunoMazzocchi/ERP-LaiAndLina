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
import java.sql.*;
import java.text.*;

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

    @RequestMapping(value="/newProduct", method=RequestMethod.GET)
    public ModelAndView newProduct(Model model, Authentication authentication) throws ParseException {
        ModelAndView modelAndView = new ModelAndView();
        model.addAttribute("newProduct", new Product());
        modelAndView.setViewName("production/newProduct.html");
        authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        User user = userService.getByEmail(userName);
        System.out.println(userName);
        modelAndView.addObject(user);
        return modelAndView;
    }
    @RequestMapping(value="/product={productId}", method=RequestMethod.GET)
    public ModelAndView editProduct(Model model, Authentication authentication,
                                   @PathVariable("productId") int productId)throws ParseException {

        Product product = productService.findById(productId).stream().findFirst().orElse(null);

        ModelAndView modelAndView = new ModelAndView();
        model.addAttribute("product", product);
        modelAndView.setViewName("production/product.html");
        authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        User user = userService.getByEmail(userName);
        modelAndView.addObject(user);



        return modelAndView;
    }

    @PostMapping("/editProductForm")
    public ModelAndView editProductForm(@ModelAttribute("currentProduct") Product product,
                                  BindingResult bindingResult,
                                  ModelMap model) {
        if(bindingResult.hasErrors()){
            model.addAttribute("index.html");
        }
        product.setState(2);
        productService.save(product);


        return new ModelAndView("redirect:/control/product/all", model);
    }
    @PostMapping("/saveProductForm")
    public ModelAndView save(@ModelAttribute("newProduct") NewProductForm productForm,
                             BindingResult bindingResult,
                             ModelMap model, Authentication authentication){
        java.sql.Date timestamp = new Date(System.currentTimeMillis());

        Product product = new Product();
        product.setName(productForm.getName());
        product.setDescription(productForm.getDescription());
        product.setPrice(productForm.getPrice());
        product.setCreationDate(timestamp);

        authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        User user = userService.getByEmail(userName);
        product.setState(1);
        product.setUserCreator(user.getId());
        productService.save(product);
        return new ModelAndView("redirect:/control/product/all", model);
    }

}
