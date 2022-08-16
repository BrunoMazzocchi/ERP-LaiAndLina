package com.laiandlina.crm.web.controller;

import com.laiandlina.crm.domain.service.*;
import com.laiandlina.crm.persistance.data.*;
import com.laiandlina.crm.persistance.entity.*;
import com.laiandlina.crm.persistance.repository.*;
import com.laiandlina.crm.web.config.exception.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.security.access.prepost.*;
import org.springframework.security.core.*;
import org.springframework.security.core.context.*;
import org.springframework.ui.*;
import org.springframework.util.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.*;
import org.springframework.web.servlet.*;
import org.springframework.web.servlet.view.*;

import javax.imageio.*;
import javax.imageio.stream.*;
import java.awt.image.*;
import java.io.*;
import java.nio.file.*;
import java.text.*;
import java.util.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleService roleService;
    @Autowired
    private DepartmentService departmentService;



    public User getCurrentUser(@CurrentUser UserPrincipal userPrincipal) {
        return userRepository.findById(userPrincipal.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userPrincipal.getId()));
    }

    @GetMapping("/myProfile")
    @PreAuthorize("hasRole('ADMIN')")
    public ModelAndView getMeUser() {
        ModelAndView modelAndView = new ModelAndView();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        User user = userService.getByEmail(userName);

        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        modelAndView.setViewName("user/profile.html");
        modelAndView.addObject("CurrentUser", getCurrentUser(userPrincipal));

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

    //The following controller will redirect you to all user list
    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public ModelAndView getAllUsers(Authentication authentication){
        ModelAndView modelAndView = new ModelAndView();
        authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        User user = userService.getByEmail(userName);

        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        modelAndView.setViewName("user/users.html");
        modelAndView.addObject("CurrentUser", getCurrentUser(userPrincipal));

        modelAndView.addObject("userList", userRepository.findAll());
        modelAndView.addObject(user);

        return modelAndView;
    }

    //Function to upload profile pictures, however they are getting saved on static directoy. Must be changed.
    @RequestMapping(value = "/changePicture", method = RequestMethod.POST)
    public ModelAndView changeProfilePicture(   @RequestParam("myPicture") MultipartFile multipartFile,
                                                @RequestParam("idUser") int idUser) throws IOException {
        String fileName = "user-" + idUser + ".jpg";
        String uploadDir = "src/main/resources/static/images/upload/";

        saveFile(uploadDir, fileName, multipartFile);
        userService.updateUserProfilePicture(fileName, idUser);
        return new ModelAndView("redirect:/user/myProfile");

    }

    public static void saveFile(String uploadDir, String fileName,
                                MultipartFile multipartFile) throws IOException {
        Path uploadPath = Paths.get(uploadDir);

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        try (InputStream inputStream = multipartFile.getInputStream()) {
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ioe) {
            throw new IOException("Could not save image file: " + fileName, ioe);
        }
    }

}
