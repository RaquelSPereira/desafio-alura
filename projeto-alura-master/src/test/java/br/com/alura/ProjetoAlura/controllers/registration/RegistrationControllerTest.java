package br.com.alura.ProjetoAlura.controllers.registration;

import br.com.alura.ProjetoAlura.dtos.registration.NewRegistrationDTO;
import br.com.alura.ProjetoAlura.models.registration.RegistrationReportItem;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.alura.ProjetoAlura.entities.course.Course;
import br.com.alura.ProjetoAlura.entities.registration.Registration;
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
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.OK;

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
        when(courseService.findByCode(newRegistrationDTO.getCourseCode())).thenReturn(course);
        when(userService.findByEmail(newRegistrationDTO.getStudentEmail())).thenReturn(student);
        when(registrationService.findByCourseCodeAndStudentId(course.getCode(), student.getId())).thenReturn(null);  // Não existe registro ainda
        when(registrationService.save(any(Registration.class))).thenReturn(new Registration());

        ResponseEntity<?> response = registrationController.newRegistration(newRegistrationDTO);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    void shouldReturnBadRequestWhenCourseIsInactive() {
        course.setStatus(CourseEnum.INACTIVE);
        when(courseService.findByCode(newRegistrationDTO.getCourseCode())).thenReturn(course);
        when(userService.findByEmail(newRegistrationDTO.getStudentEmail())).thenReturn(student);

        ResponseEntity<?> response = registrationController.newRegistration(newRegistrationDTO);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void shouldReturnBadRequestWhenStudentNotFound() {
        when(courseService.findByCode(newRegistrationDTO.getCourseCode())).thenReturn(course);
        when(userService.findByEmail(newRegistrationDTO.getStudentEmail())).thenReturn(null);

        ResponseEntity<?> response = registrationController.newRegistration(newRegistrationDTO);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void shouldReturnBadRequestWhenUserIsNotStudent() {
        student.setRole(RoleEnum.INSTRUCTOR);
        when(courseService.findByCode(newRegistrationDTO.getCourseCode())).thenReturn(course);
        when(userService.findByEmail(newRegistrationDTO.getStudentEmail())).thenReturn(student);

        ResponseEntity<?> response = registrationController.newRegistration(newRegistrationDTO);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void shouldReturnBadRequestWhenRegistrationAlreadyExists() {
        Registration existingRegistration = new Registration(1L, course, student, LocalDateTime.now());
        when(courseService.findByCode(newRegistrationDTO.getCourseCode())).thenReturn(course);
        when(userService.findByEmail(newRegistrationDTO.getStudentEmail())).thenReturn(student);
        when(registrationService.findByCourseCodeAndStudentId(course.getCode(), student.getId())).thenReturn(existingRegistration);

        ResponseEntity<?> response = registrationController.newRegistration(newRegistrationDTO);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void shouldReturnRegistrationReportSuccessfully() {
        // Mockando o retorno do service
        List<RegistrationReportItem> mockReport = Arrays.asList(
                new RegistrationReportItem("Course 1", "C001", "Instructor 1", "instructor1@example.com", 10L),
                new RegistrationReportItem("Course 2", "C002", "Instructor 2", "instructor2@example.com", 5L)
        );
        when(registrationService.findCourseRegistrationReport()).thenReturn(mockReport);

        ResponseEntity<List<RegistrationReportItem>> response = registrationController.report();
        assertEquals(OK, response.getStatusCode());
        assertEquals(2, Objects.requireNonNull(response.getBody()).size());
        assertEquals("Course 1", response.getBody().getFirst().getCourseName());
        assertEquals(10L, response.getBody().getFirst().getTotalRegistrations());
    }
}


