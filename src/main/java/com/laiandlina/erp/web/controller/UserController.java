package com.laiandlina.erp.web.controller;

import com.laiandlina.erp.domain.service.*;
import com.laiandlina.erp.persistance.data.*;
import com.laiandlina.erp.persistance.entity.*;
import com.laiandlina.erp.persistance.repository.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.security.access.prepost.*;
import org.springframework.security.core.annotation.*;
import org.springframework.ui.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.*;
import org.springframework.web.servlet.*;

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


    @GetMapping("/myProfile")
    public ModelAndView getMeUser( @AuthenticationPrincipal UserPrincipal userPrincipal) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("user/profile.html");
        modelAndView.addObject(userPrincipal);

        return modelAndView;
    }

    //The following controller will redirect you to the new User form (AuthController Sign up)
    @RequestMapping(value="/newUser", method=RequestMethod.GET)
    @PreAuthorize("hasRole('ADMIN')")
    public ModelAndView newUser(Model model,
                                 @AuthenticationPrincipal UserPrincipal userPrincipal) throws ParseException {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject(userPrincipal);
        modelAndView.addObject("roles", roleService.findAll());
        modelAndView.addObject("departments", departmentService.findAll());
        model.addAttribute("newUser", new User());

        modelAndView.setViewName("user/newUser.html");

        return modelAndView;
    }

    //The following controller will redirect you to all user list
    @RequestMapping(value = "/users", method = RequestMethod.GET)
    @PreAuthorize("hasRole('ADMIN')")
    public ModelAndView getAllUsers( @AuthenticationPrincipal UserPrincipal userPrincipal){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("user/users.html");
        modelAndView.addObject("CurrentUser", userPrincipal);
        modelAndView.addObject("userList", userRepository.findAllUser());
        Optional<VwUserDepartment> user1 = userRepository.findAllUser().stream().findFirst();
        System.out.println(user1);
        modelAndView.addObject(userPrincipal);

        return modelAndView;
    }

    //Function to upload profile pictures, however they are getting saved on static directoy. Must be changed.
    @RequestMapping(value = "/changePicture", method = RequestMethod.POST)
    public ModelAndView changeProfilePicture(   @RequestParam("myPicture") MultipartFile multipartFile,
                                                @RequestParam("idUser") int idUser) throws IOException {
        String fileName = "user-" + idUser + ".jpg";
        String uploadDir = "C:/profilePictures";

        String fileToBeSave = "http://localhost:8000/images/" + fileName;
        saveFile(uploadDir, fileName, multipartFile);
        userService.updateUserProfilePicture(fileToBeSave, idUser);
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

    @PostMapping("/disableUser={idUser}")
    public ModelAndView deleteProductClient(@PathVariable("idUser") int idUser){
        try{
            User user = userRepository.findById(idUser).stream().findFirst().orElseThrow(null);
            user.setState(3);
            user.setActive(false);
            userRepository.save(user);
            return new ModelAndView("redirect:/user/users?msg=5");
        } catch (Exception error){
            System.out.println("Error on user delete: " + error);
            return new ModelAndView("redirect:/user/users?msg=6");
        }
    }
}
