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

import javax.persistence.ManyToMany;
import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Controller
@RequestMapping("/articles")
@RequiredArgsConstructor
public class ArticleController {
    @Autowired
    private ArticleService articleService;
    @Autowired
    private UserService userService;

    @GetMapping(value="/{id}")
    public String showArticle(Model model, @PathVariable String id, RedirectAttributes redirectAttributes) {
        ArticleDTO article = articleService.findById(Long.valueOf(id));
        List<ArticleDTO> articleDTOS = article.getRelatedArticlesTitles().stream().map(entry->articleService.findById(entry)).collect(toList());
        model.addAttribute("article", article);
        model.addAttribute("relatedArticles",articleDTOS);
        return "viewArticle";
    }
    @GetMapping("/delete/{id}")
    public String delEntry(Model model, @PathVariable String id, RedirectAttributes redirectAttributes){
        Long idAuthor = articleService.getAuthorOfBook(Long.parseLong(id)).getId();
        articleService.delete(Long.parseLong(id));
       /* try {
            Thread.sleep(30000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }*/
        StringBuilder sb  = new StringBuilder("redirect:/user/");
        sb.append(idAuthor);
        return sb.toString();
    }
    @GetMapping("/update/{id}")
    public String searchEntry(Model model, @PathVariable String id) {
        ArticleDTO item = articleService.findById(Long.parseLong(id));
        model.addAttribute("idAuthor",userService.findByUsername(item.getAuthorName()).getId());
        model.addAttribute("entry", item);
        model.addAttribute("articles", articleService.findAll());
        return "updateArticle";
    }
    @PostMapping("/update/submit/{idAuthor}")
    public String updateEntry(@ModelAttribute ArticleDTO form, Model model,@PathVariable String idAuthor, RedirectAttributes redirectAttributes){
        User author = userService.findUserById(Long.valueOf(idAuthor));
        if(author==null){
            model.addAttribute("errorMessageAuthor", "Error! The current writer could not be identified!");
            return "redirect:/login";
        }
        if(form.getTitle().equals("") || form.getBody().equals("") || form.getAbstractText().equals("")){
            model.addAttribute("errorMessageAuthor", "Error! Please fill all the necessary fields(Title, Body, Abstract)!");
            return this.searchEntry(model,String.valueOf(form.getId()));
        }
        Article a = articleService.findByTitleAndAuthor(form.getTitle(),author.getUsername());
        if(a!=null && a.getId()!=form.getId()){
            model.addAttribute("errorMessageAuthor", "Error! An Article with the same Title written by the same Author already exists! If you want you can update that one from the user page.");
            return searchEntry(model,String.valueOf(form.getId()));//"createArticle";
        }
        Article article = new Article(form.getTitle(),form.getAbstractText(),author,form.getBody());
        article.setId(form.getId());
        if(form.getRelatedArticlesTitles()!=null){
            form.getRelatedArticlesTitles().stream().filter(entry->entry!=form.getId()).map(entry->articleService.findArticleById(entry)).forEach(entry->article.addRelatedArticle(entry));
        }

        if(articleService.save(article) != null){
            redirectAttributes.addFlashAttribute("success", "Training successfully added!");
        }else{
            redirectAttributes.addFlashAttribute("success", "Error!");
        }
        StringBuilder sb = new StringBuilder("redirect:/user/");
        sb.append(author.getId());
        return sb.toString();
    }

    @GetMapping("/add/{id}")
    public String getEntryForm(Model model,@PathVariable String id){
        if(!model.containsAttribute("entry")) {
            ArticleDTO item = new ArticleDTO();
            //List<ArticleDTO> articleDTOS = new ArrayList<>();
            List<Long> listOfIds = new ArrayList<>();
            item.setRelatedArticlesTitles(listOfIds);
            model.addAttribute("idAuthor",id);
            model.addAttribute("articles", articleService.findAll());
            model.addAttribute("entry", item);
        }
        return "createArticle";
    }
    //,@ModelAttribute("relatedArticles") RelatedArticlesConstructor rac
    @PostMapping("/add/submit/{idAuthor}")
    public String addEntry(@ModelAttribute ArticleDTO form, Model model,@PathVariable String idAuthor, RedirectAttributes redirectAttributes){
        if(userService.findUserById(Long.parseLong(idAuthor))==null){
            model.addAttribute("errorMessageAuthor", "Error! The current writer could not be identified!");
            return "redirect:/login";
        }
        if(form.getTitle().equals("") || form.getBody().equals("") || form.getAbstractText().equals("")){
            model.addAttribute("errorMessageAuthor", "Error! Please fill all the necessary fields(Title, Body, Abstract)!");
            return getEntryForm(model,idAuthor);//"createArticle";
        }
        if(articleService.findByTitleAndAuthor(form.getTitle(),userService.findUserById(Long.parseLong(idAuthor)).getUsername())!=null){
            model.addAttribute("errorMessageAuthor", "Error! An Article with the same Title written by the same Author already exists! If you want you can update that one from the user page.");
            return getEntryForm(model,idAuthor);//"createArticle";
        }
        Article article = new Article(form.getTitle(),form.getAbstractText(),userService.findUserById(Long.parseLong(idAuthor)),form.getBody());
        form.getRelatedArticlesTitles().stream().map(entry->articleService.findArticleById(entry)).forEach(entry->article.addRelatedArticle(entry));
        if(articleService.save(article) != null){
            redirectAttributes.addFlashAttribute("success", "Training successfully added!");
        }else{
            redirectAttributes.addFlashAttribute("success", "Error!");
        }
        StringBuilder sb = new StringBuilder("redirect:/user/");
        sb.append(idAuthor);
        return sb.toString();
    }

}
