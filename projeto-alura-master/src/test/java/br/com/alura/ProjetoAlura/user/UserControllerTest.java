package br.com.alura.ProjetoAlura.user;

import br.com.alura.ProjetoAlura.controllers.user.UserController;
import br.com.alura.ProjetoAlura.dtos.login.LoginUserDto;
import br.com.alura.ProjetoAlura.dtos.response.ErrorItemDTO;
import br.com.alura.ProjetoAlura.dtos.token.RecoveryJwtTokenDto;
import br.com.alura.ProjetoAlura.dtos.user.NewInstructorUserDTO;
import br.com.alura.ProjetoAlura.dtos.user.NewStudentUserDTO;
import br.com.alura.ProjetoAlura.dtos.user.UserListItemDTO;
import br.com.alura.ProjetoAlura.entities.user.User;
import br.com.alura.ProjetoAlura.enums.role.RoleEnum;
import br.com.alura.ProjetoAlura.services.auth.AuthenticateService;
import br.com.alura.ProjetoAlura.services.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    private User student;
    private NewStudentUserDTO newStudentUserDTO;
    private LoginUserDto loginUserDto;
    private RecoveryJwtTokenDto recoveryJwtTokenDto;
    private UserListItemDTO userListItemDTO;
    private List<UserListItemDTO> userListItemDTOList;
    private NewInstructorUserDTO newInstructorUserDTO;

    private final String validEmail = "email@email.com";

    @BeforeEach
    void setUp() {
        newStudentUserDTO = new NewStudentUserDTO("name", validEmail, "password");
        loginUserDto = new LoginUserDto(validEmail, "password");
        recoveryJwtTokenDto = new RecoveryJwtTokenDto("token");
        newInstructorUserDTO = new NewInstructorUserDTO("name", validEmail, "password");
        student = new User("Student Name", "student@mail.com", RoleEnum.STUDENT, "password");
    }

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    @Mock
    private AuthenticateService authenticateService;

    @Test
    void shouldCreateNewStudentSuccessfully() {
        when(userService.existsByEmail(newStudentUserDTO.getEmail())).thenReturn(false);

        ResponseEntity<?> response = userController.newStudent(newStudentUserDTO);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    void shouldReturnBadRequestWhenStudentAlreadyExists() {
        when(userService.existsByEmail(newStudentUserDTO.getEmail())).thenReturn(true);

        ResponseEntity<?> response = userController.newStudent(newStudentUserDTO);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void shouldAuthenticateAndReturnTokenSuccessfully() {
        when(authenticateService.authenticateUser(loginUserDto)).thenReturn(recoveryJwtTokenDto);

        ResponseEntity<?> response = userController.authenticateUser(loginUserDto);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(recoveryJwtTokenDto, response.getBody());
    }

    @Test
    void shouldReturnAllUsersSuccessfully() {
        List<User> userList = new ArrayList<>();
        userList.add(student);
        when(userService.findAll()).thenReturn(userList);

        ResponseEntity<?> response = userController.listAllUsers();
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void shouldCreateNewInstructorSuccessfully() {
        when(userService.existsByEmail(validEmail)).thenReturn(false);

        ResponseEntity<?> response = userController.newInstructor(newInstructorUserDTO);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    void shouldReturnBadRequestWhenInstructorAlreadyExists() {
        when(userService.existsByEmail(validEmail)).thenReturn(true);

        ResponseEntity<?> response = userController.newInstructor(newInstructorUserDTO);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        ErrorItemDTO errorItemDTO = (ErrorItemDTO) response.getBody();
        assertEquals("Email já cadastrado no sistema", errorItemDTO.getMessage());
    }
}