package net.bogdan.newsagency.service;

import lombok.RequiredArgsConstructor;
import net.bogdan.newsagency.model.ArticleDTO;
import net.bogdan.newsagency.model.User;
import net.bogdan.newsagency.model.UserDTO;
import net.bogdan.newsagency.repository.ArticleRepository;
import net.bogdan.newsagency.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class UserService {

    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final ArticleRepository articleRepository;
    @Autowired
    private final ArticleService as;
    public List<UserDTO> findAll() {
        return userRepository.findAll().stream()
                .map(UserDTO::fromEntity)
                .collect(toList());
    }

    public User save(User user) {
        User saved = userRepository.save(user);
        if(saved!=null){
            return saved;
        }
        return null;
    }
    public UserDTO findById(Long id){
        if(userRepository.findById(id).isPresent())
            return UserDTO.fromEntity(userRepository.findById(id).get());
        return null;
    }
    public User findUserById(Long id){
        return userRepository.findById(id).get();
    }
    public boolean delete(Long id){
        if(userRepository.findById(id).isPresent()){
            articleRepository.findAll().stream().filter(entry->entry.getAuthor().getId()==id).map(entry->entry.getId()).forEach(as::delete);
            userRepository.delete(userRepository.findById(id).get());
            return true;
        }
        return false;
    }
    public UserDTO findByUsername(String username){
        if(userRepository.findByUsername(username)==null)
            return null;
        return UserDTO.fromEntity(userRepository.findByUsername(username));
    }

}
