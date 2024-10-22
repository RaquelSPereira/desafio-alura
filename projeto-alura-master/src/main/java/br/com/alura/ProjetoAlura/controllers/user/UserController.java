package br.com.alura.ProjetoAlura.controllers.user;

import br.com.alura.ProjetoAlura.dtos.login.LoginUserDto;
import br.com.alura.ProjetoAlura.dtos.response.SuccessResponseDTO;
import br.com.alura.ProjetoAlura.dtos.token.RecoveryJwtTokenDto;
import br.com.alura.ProjetoAlura.dtos.user.NewInstructorUserDTO;
import br.com.alura.ProjetoAlura.dtos.user.NewStudentUserDTO;
import br.com.alura.ProjetoAlura.dtos.user.UserListItemDTO;
import br.com.alura.ProjetoAlura.services.auth.AuthenticateService;
import br.com.alura.ProjetoAlura.services.user.UserService;
import br.com.alura.ProjetoAlura.dtos.response.ErrorItemDTO;
import br.com.alura.ProjetoAlura.util.exceptions.CreatedException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticateService authenticateService;

    @PostMapping("/login")
    public ResponseEntity<RecoveryJwtTokenDto> authenticateUser(@RequestBody LoginUserDto loginUserDto) {
        RecoveryJwtTokenDto token = authenticateService.authenticateUser(loginUserDto);
        return new ResponseEntity<>(token, HttpStatus.OK);
    }


    @PostMapping("/user/newStudent")
    public ResponseEntity<Object> newStudent(@RequestBody @Valid NewStudentUserDTO newStudentUserDTO) {
        if(userService.existsByEmail(newStudentUserDTO.getEmail())) {
            return new ResponseEntity<>(new ErrorItemDTO("email", "Email já cadastrado no sistema"), HttpStatus.BAD_REQUEST);
        }

        userService.createStudent(newStudentUserDTO);
        return new ResponseEntity<>(new SuccessResponseDTO<>("Estudante cadastrado com sucesso!", "OK"), HttpStatus.CREATED);
    }

    @GetMapping("/user/all")
    public ResponseEntity<SuccessResponseDTO<List<UserListItemDTO>>> listAllUsers() {
        List<UserListItemDTO> users = userService.findAll().stream()
                .map(UserListItemDTO::new)
                .toList();
        return new ResponseEntity<>(new SuccessResponseDTO<>("", users), HttpStatus.OK);
    }


    @PostMapping("/user/newInstructor")
    public ResponseEntity<Object> newInstructor(@RequestBody @Valid NewInstructorUserDTO instructorUserDTO) {
        if(userService.existsByEmail(instructorUserDTO.getEmail())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorItemDTO("email", "Email já cadastrado no sistema"));
        }

        userService.createInstructor(instructorUserDTO);
        return new ResponseEntity<>(new SuccessResponseDTO<>("Instrutor cadastrado com sucesso!", "OK"), HttpStatus.CREATED);
    }

}
