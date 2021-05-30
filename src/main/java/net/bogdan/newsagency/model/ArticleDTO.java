package net.bogdan.newsagency.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Data
@Builder
@AllArgsConstructor
public class ArticleDTO {
    private Long id;
    private String title;
    private String abstractText;
    private String authorName;
    private String body;
    private List<Long> relatedArticlesTitles;
    public ArticleDTO(){

    }
    public static ArticleDTO fromEntity(Article article) {
        return ArticleDTO.builder()
                .id(article.getId())
                .title(article.getTitle())
                .authorName(article.getAuthor().getUsername())
                .abstractText(article.getAbstractText())
                .body(article.getBody())
                .relatedArticlesTitles(article.getRelatedArticles().stream().map(entry->entry.getId()).collect(Collectors.toList()))
                .build();
    }
    public void addRelatedArticle(ArticleDTO a){
        this.relatedArticlesTitles.add(a.getId());
    }
}
