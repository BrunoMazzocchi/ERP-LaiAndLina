package com.laiandlina.erp.web.controller;


import com.laiandlina.erp.domain.service.*;
import com.laiandlina.erp.persistance.data.*;
import com.laiandlina.erp.persistance.entity.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.security.core.annotation.*;
import org.springframework.ui.*;
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
    public ModelAndView getAllProducts(HttpServletRequest request,
                                       @AuthenticationPrincipal UserPrincipal userPrincipal) {
        ModelAndView modelAndView = new ModelAndView();
        try{
            modelAndView.setViewName("production/products.html");
            modelAndView.addObject("products", productService.findAll());
            modelAndView.addObject(userPrincipal);
            return modelAndView;
        } catch (Exception error){
            System.out.println("Error on redirect to all product: " + error);
            return new ModelAndView("redirect:/index");
        }
    }

    @RequestMapping(value="/newProduct", method=RequestMethod.GET)
    public ModelAndView newProduct(Model model,
                                   @AuthenticationPrincipal UserPrincipal userPrincipal) throws ParseException {
        try {
            ModelAndView modelAndView = new ModelAndView();
            model.addAttribute("newProduct", new Product());
            modelAndView.setViewName("production/newProduct.html");
            modelAndView.addObject(userPrincipal);
            return modelAndView;
        } catch (Exception error){
            System.out.println("Error on redirect to save product: " + error);
            return new ModelAndView("redirect:/control/product/all");
        }
    }


    @RequestMapping(value="/product={productId}", method=RequestMethod.GET)
    public ModelAndView editProduct(Model model, @AuthenticationPrincipal UserPrincipal userPrincipal,
                                   @PathVariable("productId") int productId)throws ParseException {
        try {
            Product product = productService.findById(productId).stream().findFirst().orElse(null);
            ModelAndView modelAndView = new ModelAndView();
            model.addAttribute("product", product);
            modelAndView.setViewName("production/product.html");
            modelAndView.addObject(userPrincipal);
            return modelAndView;
        } catch (Exception error){
            System.out.println("Error on redirect to edit product: " + error);
            return new ModelAndView("redirect:/control/product/all");
        }
    }

    @PostMapping("/editProductForm")
    public ModelAndView editProductForm(@ModelAttribute("currentProduct") Product product,
                                  ModelMap model) {
        try{
            product.setState(2);
            productService.save(product);
            return new ModelAndView("redirect:/control/product/all?msg=3", model);
        } catch (Exception error){
            System.out.println("Error on edit product: " + error);
            return new ModelAndView("redirect:/control/product/all?msg=4", model);
        }

    }
    @PostMapping("/saveProductForm")
    public ModelAndView save(@ModelAttribute("newProduct") NewProductForm productForm,
                             ModelMap model, @AuthenticationPrincipal UserPrincipal userPrincipal){
        try{
            java.sql.Date timestamp = new Date(System.currentTimeMillis());

            Product product = new Product();
            product.setName(productForm.getName());
            product.setDescription(productForm.getDescription());
            product.setPrice(productForm.getPrice());
            product.setCreationDate(timestamp);
            product.setState(1);
            product.setUserCreator(userPrincipal.getId());
            productService.save(product);
            return new ModelAndView("redirect:/control/product/all?msg=1", model);
        } catch (Exception error){
            System.out.println("Error on save product: " + error);
            return new ModelAndView("redirect:/control/product/all?msg=2", model);

        }
    }

    @PostMapping("/deleteProduct={idProduct}")
    public ModelAndView deleteProduct(@PathVariable("idProduct") int idProduct){
        try{
            Product product = productService.findById(idProduct).stream().findFirst().orElse(null);
            product.setState(3);
            productService.save(product);
            return new ModelAndView("redirect:/control/product/all?msg=5");
        } catch (Exception error){
            System.out.println("Error on client save: " + error);
            return new ModelAndView("redirect:/control/product/all?msg=6");
        }
    }

}
