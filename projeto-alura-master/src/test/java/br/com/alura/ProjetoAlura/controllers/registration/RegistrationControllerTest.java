package br.com.alura.ProjetoAlura.controllers.registration;

import br.com.alura.ProjetoAlura.dtos.registration.NewRegistrationDTO;
import br.com.alura.ProjetoAlura.util.exceptions.BadRequestException;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.alura.ProjetoAlura.entities.course.Course;
import br.com.alura.ProjetoAlura.entities.user.User;
import br.com.alura.ProjetoAlura.enums.course.CourseEnum;
import br.com.alura.ProjetoAlura.enums.role.RoleEnum;
import br.com.alura.ProjetoAlura.services.course.CourseService;
import br.com.alura.ProjetoAlura.services.registration.RegistrationService;
import br.com.alura.ProjetoAlura.services.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RegistrationControllerTest {

    @InjectMocks
    private RegistrationController registrationController;

    @Mock
    private CourseService courseService;

    @Mock
    private UserService userService;

    @Mock
    private RegistrationService registrationService;

    private NewRegistrationDTO newRegistrationDTO;
    private Course course;
    private User instructor;
    private User student;

    @BeforeEach
    void setUp() {
        newRegistrationDTO = new NewRegistrationDTO("JAVA-AD", "student@mail.com");
        student = new User("Student Name", "student@mail.com", RoleEnum.STUDENT, "password");
        instructor = new User("Instructor Name", "instructor@mail.com", RoleEnum.INSTRUCTOR, "password");
        course = new Course(1L, "Java Course", "JAVA-AD", instructor, "Advanced Java", CourseEnum.ACTIVE, null, LocalDateTime.now());
    }

    @Test
    void shouldCreateNewRegistrationSuccessfully() {
        when(courseService.findByCode(newRegistrationDTO.getCourseCode())).thenReturn(Optional.ofNullable(course));
        when(userService.findByEmail(newRegistrationDTO.getStudentEmail())).thenReturn(Optional.ofNullable(student));
        when(registrationService.findByCourseCodeAndStudentId(course.getCode(), student.getId())).thenReturn(Optional.empty());

        ResponseEntity<?> response = registrationController.newRegistration(newRegistrationDTO);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    void shouldReturnBadRequestWhenCourseIsInactive() {
        course.setStatus(CourseEnum.INACTIVE);
        when(courseService.findByCode(newRegistrationDTO.getCourseCode())).thenReturn(Optional.ofNullable(course));
        when(userService.findByEmail(newRegistrationDTO.getStudentEmail())).thenReturn(Optional.ofNullable(student));

        try {
            registrationController.newRegistration(newRegistrationDTO);
        } catch (BadRequestException e) {
            assertEquals("Curso inativado", e.getMessage());
        }
    }

    @Test
    void shouldReturnBadRequestWhenStudentNotFound() {
        when(courseService.findByCode(newRegistrationDTO.getCourseCode())).thenReturn(Optional.ofNullable(course));
        when(userService.findByEmail(newRegistrationDTO.getStudentEmail())).thenReturn(Optional.empty());

        try {
            registrationController.newRegistration(newRegistrationDTO);
        } catch (BadRequestException e) {
            assertEquals("Usuário não encontrado", e.getMessage());
        }
    }

    @Test
    void shouldReturnBadRequestWhenUserIsNotStudent() {
        when(courseService.findByCode(newRegistrationDTO.getCourseCode())).thenReturn(Optional.ofNullable(course));
        when(userService.findByEmail(newRegistrationDTO.getStudentEmail())).thenReturn(Optional.ofNullable(instructor));

        try {
            registrationController.newRegistration(newRegistrationDTO);
        } catch (BadRequestException e) {
            assertEquals("Email informado inválido", e.getMessage());
        }
    }
}


