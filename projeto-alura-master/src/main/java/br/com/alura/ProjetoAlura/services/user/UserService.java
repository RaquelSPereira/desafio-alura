package br.com.alura.ProjetoAlura.services.user;

import br.com.alura.ProjetoAlura.entities.User;
import br.com.alura.ProjetoAlura.repository.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User findByEmail(String emailInstructor){
        return userRepository.findByEmail(emailInstructor);

    }
}
