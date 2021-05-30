package net.bogdan.newsagency.controller;

import lombok.RequiredArgsConstructor;
import net.bogdan.newsagency.model.*;
import net.bogdan.newsagency.service.ArticleService;
import net.bogdan.newsagency.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private ArticleService articleService;

    @GetMapping(value = "/{id}")
    public String renderHomePage(Model model, @PathVariable String id, RedirectAttributes redirectAttributes) {
        model.addAttribute("articles", articleService.findAllWithAuthor(Long.valueOf(id)));
        model.addAttribute("idUser", id);
        return "viewUser";
    }

    @GetMapping(value="//articles/{id}")
    public String showArticle(Model model, @PathVariable String id, RedirectAttributes redirectAttributes) {
        ArticleDTO article = articleService.findById(Long.valueOf(id));
        List<ArticleDTO> articleDTOS = article.getRelatedArticlesTitles().stream().map(entry->articleService.findById(entry)).collect(toList());
        model.addAttribute("article", article);
        model.addAttribute("idAuthor",articleService.findArticleById(Long.parseLong(id)).getAuthor().getId());
        model.addAttribute("relatedArticles",articleDTOS);
        return "viewArticleUser";
    }
    @GetMapping(value = "/add/")
    public String initCreateWriter(Model model) {
        model.addAttribute("entry", new UserDTO());
        return "addUser";
    }
    @PostMapping("/add/submit/")
    public String addEntry(@ModelAttribute UserDTO form, Model model,  RedirectAttributes redirectAttributes){
        if(form.getPassword().equals("") || form.getUsername().equals("") || form.getName().equals("") || form.getPnc().equals("")){
            model.addAttribute("errorMessageAuthor", "Error! Please fill all the necessary fields(Name,Username, Password, PNC)!");
            return initCreateWriter(model);
        }
        if(userService.findByUsername(form.getUsername())!=null){
            model.addAttribute("errorMessageAuthor", "Error! A user with the same username already exists! If you want you can update that one from the admin page.");
            return initCreateWriter(model);
        }
        User user = new User(form.getName(),form.getPnc(),form.getUsername(),form.getPassword());
        user.setRole(Type.WRITER);
        if(userService.save(user) != null){
            redirectAttributes.addFlashAttribute("success", "Training successfully added!");
        }else{
            redirectAttributes.addFlashAttribute("success", "Error!");
            return initCreateWriter(model);
        }
        StringBuilder sb = new StringBuilder("redirect:/admin/");
        return sb.toString();
    }
    @GetMapping("/delete/{id}")
    public String delEntry(Model model, @PathVariable String id, RedirectAttributes redirectAttributes){
        userService.delete(Long.parseLong(id));
        StringBuilder sb  = new StringBuilder("redirect:/admin/");
        return sb.toString();
    }
    @GetMapping("/update/{id}")
    public String searchEntry(Model model, @PathVariable String id) {
        UserDTO item = userService.findById(Long.parseLong(id));
        model.addAttribute("entry", item);
        return "updateUser";
    }
    @PostMapping("/update/submit/")
    public String updateEntry(@ModelAttribute UserDTO form, Model model, RedirectAttributes redirectAttributes){
        if(form.getPassword().equals("") || form.getUsername().equals("") || form.getName().equals("") || form.getPnc().equals("")){
            model.addAttribute("errorMessageAuthor", "Error! Please fill all the necessary fields(Name,Username, Password, PNC)!");
            return searchEntry(model,String.valueOf(form.getId()));
        }
        UserDTO userDTO = userService.findByUsername(form.getUsername());
        if(userDTO!=null && userDTO.getId()!=form.getId()){
            model.addAttribute("errorMessageAuthor", "Error! A user with the same username already exists! If you want you can update that one from the admin page.");
            return searchEntry(model,String.valueOf(form.getId()));
        }
        User user = new User(form.getName(),form.getPnc(),form.getUsername(),form.getPassword());
        user.setId(form.getId());
        user.setRole(Type.WRITER);
        List<Article> writtenArticles = articleService.findAllArticlesWithAuthor(form.getId());
        if(writtenArticles!=null) {
            user.setWrittenArticles(writtenArticles);
        }
     //   User newUser = userService.save(user);
        if(userService.save(user) != null){
            redirectAttributes.addFlashAttribute("success", "Training successfully added!");
//                writtenArticles.stream().map(entry->entry.getAuthor().getName()).forEach(System.out::println);
//                writtenArticles.stream().forEach(entry->entry.setAuthor(newUser));
          //  }
        }else{
            redirectAttributes.addFlashAttribute("success", "Error!");
            return searchEntry(model,String.valueOf(form.getId()));
        }
        StringBuilder sb = new StringBuilder("redirect:/admin/");
        return sb.toString();
    }
}
