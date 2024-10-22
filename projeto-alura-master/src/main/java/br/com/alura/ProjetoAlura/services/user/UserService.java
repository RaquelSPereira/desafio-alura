package br.com.alura.ProjetoAlura.services.user;

import br.com.alura.ProjetoAlura.dtos.login.LoginUserDto;
import br.com.alura.ProjetoAlura.dtos.token.RecoveryJwtTokenDto;
import br.com.alura.ProjetoAlura.dtos.user.NewInstructorUserDTO;
import br.com.alura.ProjetoAlura.dtos.user.NewStudentUserDTO;
import br.com.alura.ProjetoAlura.entities.user.User;
import br.com.alura.ProjetoAlura.enums.role.RoleEnum;
import br.com.alura.ProjetoAlura.repositories.user.UserRepository;
import br.com.alura.ProjetoAlura.adapters.auth.JwtTokenAdapter;
import br.com.alura.ProjetoAlura.services.userDetails.UserDetailsImpl;
import br.com.alura.ProjetoAlura.util.encrypts.EncryptUtil;
import br.com.alura.ProjetoAlura.util.exceptions.UnauthorizedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtTokenAdapter jwtTokenService;

    @Autowired
    private AuthenticationManager authenticationManager;

    public void  save (User user){
        userRepository.save(user);
    }

    public Optional<User> findByEmail(String emailInstructor){
        return userRepository.findByEmail(emailInstructor);
    }

    public boolean existsByEmail(String email){
        return userRepository.existsByEmail(email);
    }

    public Optional<User> findByEmailAndPassword(String email, String password){
        return userRepository.findByEmailAndPassword(email, password);
    }

    public List<User> findAll(){
        return userRepository.findAll();
    }

    public boolean isInstructor(String email){
        return findByEmail(email).get().getRole().equals(RoleEnum.INSTRUCTOR);
    }

    public boolean isStudent(String email){
        return findByEmail(email).get().getRole().equals(RoleEnum.STUDENT);
    }

    public User createStudent(NewStudentUserDTO newStudentUserDTO){
        User user = newStudentUserDTO.toModel();
        save(user);
        return user;
    }

    public User createInstructor(NewInstructorUserDTO newInstructorUserDTO){
        User user = newInstructorUserDTO.toModel();
        userRepository.save(user);
        return user;
    }
}
