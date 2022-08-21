package com.laiandlina.crm.web.controller;

import com.laiandlina.crm.domain.service.*;
import com.laiandlina.crm.persistance.data.*;
import com.laiandlina.crm.persistance.entity.*;
import com.laiandlina.crm.persistance.repository.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.security.core.*;
import org.springframework.security.core.annotation.*;
import org.springframework.security.core.context.*;
import org.springframework.ui.*;
import org.springframework.validation.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.*;

import java.sql.Date;
import java.util.*;

@RestController
@RequestMapping("/note")
public class NoteController {
    @Autowired
    private NoteRepository noteRepository;
    @Autowired
    private EmailService javaMailSender;
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;

    @RequestMapping(value="/note={productClientId}", method=RequestMethod.GET)
    public List<VwNote> getNotesByProductClient(@PathVariable ("productClientId") int productClientId,
                                                @AuthenticationPrincipal UserPrincipal userPrincipal){
        return noteRepository.findNoteByProductClient(productClientId);
    }

    @RequestMapping(value = "/saveNoteForm", method = RequestMethod.POST)
    public ModelAndView saveOrder(@ModelAttribute("newNote") Note note,
                                  BindingResult bindingResult,
                                  ModelMap model) {

        if(bindingResult.hasErrors()){
            model.addAttribute("index.html");
        }
        java.sql.Date timestamp = new Date(System.currentTimeMillis());
        note.setPostedOn(timestamp);


        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        User user = userService.getByEmail(userName);

        Set<User> users = new HashSet<>();
        List<User> usersByRole = userRepository.findByRoles(1);

        usersByRole.forEach(userInRole -> {
            User userForFollowUp = userInRole;
            users.add(userForFollowUp);
        });

        users.add(user);
        note.getProductClient().setUsers(users);


        users.forEach(userForEmail -> {
            javaMailSender.sendEmail(userForEmail.getEmail(), note.getTitle(),
                    "Se ha agregado una" +
                            " nueva nota. '" + note.getUser().getFirstName()  + " " + note.getUser().getLastName() + "," +
                            " ha dicho : " +
                            note.getDescription() + "'." +
                            ". Puedes continuar aqui http://localhost:8000/control/order/order=" + note.getProductClient().getId());
        });




        noteRepository.save(note);

        return new ModelAndView("redirect:/control/order/order=" + note.getProductClient().getId() + "", model);
    }

}
