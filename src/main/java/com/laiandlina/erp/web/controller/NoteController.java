package com.laiandlina.erp.web.controller;

import com.laiandlina.erp.domain.service.*;
import com.laiandlina.erp.persistance.entity.*;
import com.laiandlina.erp.persistance.repository.*;
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

        String subject = "Pedido: #" + note.getProductClient().getId();
        String body = "Se ha agregado una nueva nota.  " + note.getTitle() + ". "  +
                "publicada por " +  note.getUser().getFirstName() + " " +  note.getUser().getLastName() + ". " +
                "Ha dicho: " + note.getDescription();

        javaMailSender.sendEmailToUser(subject, body, users);
        noteRepository.save(note);

        if (note.getProductClient().getState() == 4){
            return new ModelAndView("redirect:/control/order/orderCompleted=" + note.getProductClient().getId() + "", model);
        } else {
            return new ModelAndView("redirect:/control/order/order=" + note.getProductClient().getId() + "", model);
        }
    }

}
