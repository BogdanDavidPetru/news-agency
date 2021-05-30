package net.bogdan.newsagency.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Article{ //extends AbstractEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    @Column(columnDefinition="MEDIUMTEXT")
    private String abstractText;
    @OneToOne
    private User author;
    @Column(columnDefinition="LONGTEXT")
    private String body;
    @ManyToMany
    @JoinTable(
            name = "article_related",
            joinColumns =
                    { @JoinColumn(
                            name = "article_id") },
            inverseJoinColumns =
                    { @JoinColumn(
                            name = "related_article_id") })
    private List<Article> relatedArticles;

    public Article(String title, String abstractText, User author, String body) {
        this.title = title;
        this.abstractText=abstractText;
        this.author=author;
        this.body = body;
        this.relatedArticles = new ArrayList<>();
    }
    public void addRelatedArticle(Article a){
        this.relatedArticles.add(a);
    }
    public void deleteArticle(Article a){
        this.relatedArticles.remove(a);
    }
}
