package br.com.alura.ProjetoAlura.controllers.course;

import br.com.alura.ProjetoAlura.dtos.course.NewCourseDTO;
import br.com.alura.ProjetoAlura.entities.course.Course;
import br.com.alura.ProjetoAlura.entities.user.User;
import br.com.alura.ProjetoAlura.enums.course.CourseEnum;
import br.com.alura.ProjetoAlura.services.course.CourseService;
import br.com.alura.ProjetoAlura.services.user.UserService;
import br.com.alura.ProjetoAlura.util.exceptions.BadRequestException;
import br.com.alura.ProjetoAlura.util.exceptions.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CourseControllerTest {


    @InjectMocks
    private CourseController courseController;

    @Mock
    private UserService userService;

    @Mock
    private CourseService courseService;

    private NewCourseDTO validCourse;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        validCourse = new NewCourseDTO();
        validCourse.setInstructorEmail("valid.instructor@example.com");
        validCourse.setCode("COURSE-ABV");
        validCourse.setName("Curso Exemplo");
        validCourse.setDescription("Descrição do Curso");
    }

    @Test
    void shouldCreateCourseSuccessfully() {
        when(userService.isInstructor(validCourse.getInstructorEmail())).thenReturn(true);
        when(courseService.validateUniqueCodeCourse(validCourse.getCode())).thenReturn(true);
        User mockUser = new User();
        Course expectedCourse = new Course(null, validCourse.getName(), validCourse.getCode(),
                mockUser, validCourse.getDescription(), CourseEnum.ACTIVE, null, LocalDateTime.now());
        when(courseService.createCourse(validCourse)).thenReturn(expectedCourse);
        var response = courseController.createCourse(validCourse);

        assertEquals("Curso criado com sucesso!", response.getBody().getMessage());
    }

    @Test
    // fala que o email nao perternce ao instrutor
    void shouldReturnBadRequestWhenEmail() {
        when(userService.isInstructor(validCourse.getInstructorEmail())).thenReturn(false);

        try {
            courseController.createCourse(validCourse);
        } catch (NotFoundException e) {
            assertEquals("Email informado não pertence a um instrutor", e.getMessage());
        }
    }


    @Test
    //flaa que o curso nao é valido
    void shouldReturnBadRequestWhenCourseCode() {
        when(userService.isInstructor(validCourse.getInstructorEmail())).thenReturn(true);
        when(courseService.validateUniqueCodeCourse(validCourse.getCode())).thenReturn(true);

        try {
            courseController.createCourse(validCourse);
        } catch (BadRequestException e) {
            assertEquals("Código do curso já existe", e.getMessage());
        }
    }

    @Test
    void shouldReturnBadRequestWhenCourseCodeIsNotUnique() {
        validCourse.setCode("invalid_code");
        when(userService.isInstructor(validCourse.getInstructorEmail())).thenReturn(true);
        when(courseService.validateUniqueCodeCourse(validCourse.getCode())).thenReturn(true);

        try {
            courseController.createCourse(validCourse);
        } catch (BadRequestException e) {
            assertEquals("Código do curso inválido", e.getMessage());
        }
    }
}


