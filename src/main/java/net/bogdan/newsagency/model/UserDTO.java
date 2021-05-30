package net.bogdan.newsagency.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private String name;
    private Long id;
    private String pnc;
    private String username;
    private String password;
    private List<ArticleDTO> writtenArticles;
    @Enumerated(EnumType.STRING)
    private Type role;
    public UserDTO(String name, String pnc, String username, String password){
        this.name=name;
        this.pnc=pnc;
        this.username=username;
        this.password=password;
    }

    public static UserDTO fromEntity(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .pnc(user.getPnc())
                .username(user.getUsername())
                .password(user.getPassword())
                .role(user.getRole())
                .writtenArticles(user.getWrittenArticles().stream().map(ArticleDTO::fromEntity).collect(Collectors.toList()))
                .build();
    }
}
