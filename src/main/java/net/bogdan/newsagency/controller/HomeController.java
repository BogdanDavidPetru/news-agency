package net.bogdan.newsagency.controller;

import lombok.RequiredArgsConstructor;
import net.bogdan.newsagency.model.ArticleDTO;
import net.bogdan.newsagency.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/home")
@RequiredArgsConstructor
public class HomeController {
    @Autowired
    private ArticleService articleService;

    @GetMapping(value = "")
    public String renderHomePage(Model model, HttpServletRequest request){
        //Map<String, ?> inputFlashMap = RequestContextUtils.getInputFlashMap(request);
        model.addAttribute("articles", articleService.findAll());
        return "home";
    }
    /*public List<ArticleDTO> findAll() {
        return articleService.findAll();
    }*/
}
