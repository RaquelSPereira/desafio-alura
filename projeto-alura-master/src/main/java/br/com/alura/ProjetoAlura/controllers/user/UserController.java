package br.com.alura.ProjetoAlura.controllers.user;

import br.com.alura.ProjetoAlura.dtos.login.LoginUserDto;
import br.com.alura.ProjetoAlura.dtos.token.RecoveryJwtTokenDto;
import br.com.alura.ProjetoAlura.dtos.user.NewInstructorUserDTO;
import br.com.alura.ProjetoAlura.dtos.user.NewStudentUserDTO;
import br.com.alura.ProjetoAlura.entities.user.User;
import br.com.alura.ProjetoAlura.dtos.user.UserListItemDTO;
import br.com.alura.ProjetoAlura.repositories.user.UserRepository;
import br.com.alura.ProjetoAlura.services.auth.AuthenticateService;
import br.com.alura.ProjetoAlura.services.user.UserService;
import br.com.alura.ProjetoAlura.dtos.Error.ErrorItemDTO;
import br.com.alura.ProjetoAlura.util.exceptions.CreatedException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserController {

    private  UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticateService authenticateService;


    @Transactional
    @PostMapping("/user/newStudentUserDTO")
    public ResponseEntity newStudent(@RequestBody @Valid NewStudentUserDTO newStudentUserDTO) {
        if(userService.existsByEmail(newStudentUserDTO.getEmail())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorItemDTO("email", "Email já cadastrado no sistema"));
        }

        userService.createStudent(newStudentUserDTO);
        throw new CreatedException("Estudante cadastrado com sucesso");
    }

    @GetMapping("/user/all")
    public ResponseEntity<List<UserListItemDTO>> listAllUsers() {
        List<UserListItemDTO> users = userService.findAll().stream()
                .map(UserListItemDTO::new)
                .toList();
        return ResponseEntity.status(HttpStatus.OK).body(users);
    }



    @Transactional
    @PostMapping("/user/newInstructor")
    public ResponseEntity<Object> newIsntructor(@RequestBody @Valid NewInstructorUserDTO instructorUserDTO) {
        if(userRepository.existsByEmail(instructorUserDTO.getEmail())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorItemDTO("email", "Email já cadastrado no sistema"));
        }

        User user = instructorUserDTO.toModel();
        userRepository.save(user);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


    @PostMapping("/login")
    public ResponseEntity<RecoveryJwtTokenDto> authenticateUser(@RequestBody LoginUserDto loginUserDto) {
        RecoveryJwtTokenDto token = authenticateService.authenticateUser(loginUserDto);
        return new ResponseEntity<>(token, HttpStatus.OK);
    }

}
