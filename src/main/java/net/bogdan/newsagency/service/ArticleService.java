package net.bogdan.newsagency.service;

import lombok.RequiredArgsConstructor;
import net.bogdan.newsagency.model.Article;
import net.bogdan.newsagency.model.ArticleDTO;
import net.bogdan.newsagency.model.User;
import net.bogdan.newsagency.model.UserDTO;
import net.bogdan.newsagency.repository.ArticleRepository;
import net.bogdan.newsagency.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.DelegatingServerHttpResponse;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class ArticleService {
    @Autowired
    private final ArticleRepository articleRepository;
    @Autowired
    private final UserRepository userRepository;

    public List<ArticleDTO> findAll() {
        return articleRepository.findAll().stream()
                .map(ArticleDTO::fromEntity)
                .collect(toList());
    }

    public List<ArticleDTO> findAllWithAuthor(Long userId) {
        if(userRepository.findById(userId).isPresent()){
            User author =userRepository.findById(userId).get();

            return articleRepository.findAllByAuthor(author).stream()
                    .map(ArticleDTO::fromEntity)
                    .collect(toList());
        }
        return null;

    }
    public List<Article> findAllArticlesWithAuthor(Long userId) {
        if(userRepository.findById(userId).isPresent()){
            User author =userRepository.findById(userId).get();

            return articleRepository.findAllByAuthor(author);
        }
        return null;

    }
    public ArticleDTO save(Article article) {
     //   User author = userRepository.findByUsername(articleDTO.getAuthorName());
     //   Article article = new Article(articleDTO.getTitle(),articleDTO.getAbstractText(),author,articleDTO.getBody());
        //article.setRelatedArticles(articleDTO.getRelatedArticlesTitles().stream().map(entry->articleRepository.find));
        Article result = articleRepository.save(article);
        if(result.getRelatedArticles()==null)
            result.setRelatedArticles(new ArrayList<>());
        return ArticleDTO.fromEntity(result);
    }
    public ArticleDTO findById(Long id){
        return ArticleDTO.fromEntity(articleRepository.findById(id).get());
    }
    public boolean delete(Long id){
        if(articleRepository.findById(id).isPresent()){
            Article article = articleRepository.findById(id).get();
            articleRepository.findAll().stream().forEach(entry->entry.deleteArticle(article));
            articleRepository.delete(article);
            return true;
        }
        return false;
    }
    public UserDTO getAuthorOfBook(Long id){
        if(articleRepository.findById(id).isPresent()){
            return UserDTO.fromEntity(articleRepository.findById(id).get().getAuthor());
        }
        return null;
    }
    public Article findArticleById(Long id){
        return articleRepository.findById(id).get();
    }
    public Article findByTitleAndAuthor(String title, String author){
        if(userRepository.findByUsername(author)==null)
            return null;
        return articleRepository.findByTitleAndAuthor(title,userRepository.findByUsername(author));
    }

}
