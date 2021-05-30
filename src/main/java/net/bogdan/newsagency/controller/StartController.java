package net.bogdan.newsagency.controller;

import lombok.RequiredArgsConstructor;
import net.bogdan.newsagency.model.Type;
import net.bogdan.newsagency.model.User;
import net.bogdan.newsagency.model.UserDTO;
import net.bogdan.newsagency.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;

import static java.util.stream.Collectors.toList;

@Controller
@RequiredArgsConstructor
public class StartController {
    @Autowired
    private UserService userService;
    @GetMapping(value = "")
    public String renderHomePage(Model model){
        return "redirect:/home";
    }
    @GetMapping("/login")
    public String getEntryForm(Model model){
        if(!model.containsAttribute("entry")) {
            User user = new User();
            model.addAttribute("entry", user);
        }
        return "login";
    }
    @PostMapping("/login/submit")
    public String login(@ModelAttribute User form, Model model, RedirectAttributes redirectAttributes){
        UserDTO user = userService.findByUsername(form.getUsername());
        if(user ==null || !user.getPassword().equals(form.getPassword())){
            model.addAttribute("entry", new User());
            model.addAttribute("errorMessage", "Invalid credentials");
            return "login";
        }
        if(user.getRole()== Type.WRITER){
            StringBuilder sb =  new StringBuilder("redirect:/user/");
            sb.append(user.getId());
            return sb.toString();
        }

        return "redirect:/admin/";
    }
    @GetMapping(value = "/admin/")
    public String renderAdmin(Model model) {
        model.addAttribute("users", userService.findAll().stream().filter(entry->entry.getRole()==Type.WRITER).collect(toList()));
        return "admin";
    }

}
