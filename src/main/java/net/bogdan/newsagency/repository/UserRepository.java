package net.bogdan.newsagency.repository;

import net.bogdan.newsagency.model.Article;
import net.bogdan.newsagency.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}