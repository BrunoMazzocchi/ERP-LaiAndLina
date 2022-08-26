package com.laiandlina.erp.web.controller;

import com.laiandlina.erp.domain.service.*;
import com.laiandlina.erp.persistance.entity.*;
import com.laiandlina.erp.persistance.repository.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.security.access.prepost.*;
import org.springframework.security.core.annotation.*;
import org.springframework.ui.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.*;

import java.sql.*;
import java.text.*;

@RestController
@RequestMapping("/control/post")
@PreAuthorize("hasRole('ADMIN')")
public class PostController {
    @Autowired
    PostRepository postRepository;

    @RequestMapping(value="/newPost", method=RequestMethod.GET)
    @PreAuthorize("hasRole('ADMIN')")
    public ModelAndView newPost(Model model,
                                 @AuthenticationPrincipal UserPrincipal userPrincipal) throws ParseException {
        try{
            ModelAndView modelAndView = new ModelAndView();
            modelAndView.addObject(userPrincipal);
            modelAndView.setViewName("post/newPost.html");
            return modelAndView;
        } catch(Exception error){
            System.out.println("Error on new post form: " + error);
            return new ModelAndView("redirect: /index");
        }


    }

    @PostMapping("/saveNewPost")
    public ModelAndView saveNewPost(@ModelAttribute("post") Post post,
                                     @AuthenticationPrincipal UserPrincipal userPrincipal) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject(userPrincipal);
        modelAndView.setViewName("redirect:/index");
        try{
            java.sql.Date timestamp = new Date(System.currentTimeMillis());

            Post postToSave = new Post();
            postToSave.setTitle(post.getTitle());
            postToSave.setDescription(post.getDescription());
            postToSave.setUser(userPrincipal.getId());
            postToSave.setDate(timestamp);

            postRepository.save(postToSave);
            return  modelAndView;

        } catch(Exception error){
            System.out.println("Error on new post form: " + error);
            return modelAndView;
        }
    }


    @RequestMapping(value="/posts", method=RequestMethod.GET)
    @PreAuthorize("hasRole('ADMIN')")
    public ModelAndView getAllPost(Model model,
                                @AuthenticationPrincipal UserPrincipal userPrincipal) throws ParseException {
        try{
            ModelAndView modelAndView = new ModelAndView();
            modelAndView.addObject(userPrincipal);
            modelAndView.addObject("posts", postRepository.findByUserId(userPrincipal.getId()));
            modelAndView.setViewName("post/posts.html");
            return modelAndView;
        } catch(Exception error){
            System.out.println("Error on new post form: " + error);
            return new ModelAndView("redirect: /index");
        }
    }

    @PostMapping("/deletePost={idPost}")
    public ModelAndView deleteProductClient(@PathVariable("idPost") int idPost){
        try{
            Post post = postRepository.findById(idPost);
            postRepository.deleteById(post.getId());
            return new ModelAndView("redirect:/control/post/posts?msg=5");
        } catch (Exception error){
            System.out.println("Error on post delete: " + error);
            return new ModelAndView("redirect:/control/post/posts?msg=6");
        }
    }

}
