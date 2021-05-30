package net.bogdan.newsagency.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {//extends AbstractEntity{
    private String name;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String pnc;

    private String username;

    private String password;
    @OneToMany
    @JoinColumn(name="author_id")
    private List<Article> writtenArticles;
    @Enumerated(EnumType.STRING)
    private Type role;
    public User(String name, String pnc, String username, String password){
        this.name=name;
        this.pnc=pnc;
        this.username=username;
        this.password=password;
        this.writtenArticles = new ArrayList<>();
    }


}
