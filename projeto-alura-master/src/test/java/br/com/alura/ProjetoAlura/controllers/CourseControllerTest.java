package br.com.alura.ProjetoAlura.controllers;

import br.com.alura.ProjetoAlura.controllers.course.CourseController;
import br.com.alura.ProjetoAlura.dtos.course.NewCourseDTO;
import br.com.alura.ProjetoAlura.entities.Course;
import br.com.alura.ProjetoAlura.entities.User;
import br.com.alura.ProjetoAlura.enums.role.RoleEnum;
import br.com.alura.ProjetoAlura.services.course.CourseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.mock.http.server.reactive.MockServerHttpRequest.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class CourseControllerTest {


    @InjectMocks
    private CourseController courseController;

    @Mock
    private CourseService courseService;

    private NewCourseDTO validCourse;
    private User instructor;

    @BeforeEach
    void setUp() {
        validCourse = new NewCourseDTO("Java Course", "JAVA-AD", "Advanced Java", "instructor@mail.com");
        instructor = new User("Instructor Name", "instructor@mail.com", RoleEnum.INSTRUCTOR, "password");
    }

    @Test
    void shouldCreateCourseSuccessfully() {
        when(courseService.validateEmailInstructor(validCourse.getInstructorEmail())).thenReturn(instructor);
        when(courseService.validateUniqueCodeCourse(validCourse.getCode())).thenReturn(true);
        when(courseService.save(any(Course.class))).thenReturn(new Course());

        ResponseEntity<?> response = courseController.createCourse(validCourse);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    void shouldReturnUnauthorizedWhenInstructorIsStudent() {
        instructor.setRole(RoleEnum.STUDENT);
        when(courseService.validateEmailInstructor(validCourse.getInstructorEmail())).thenReturn(instructor);

        ResponseEntity<?> response = courseController.createCourse(validCourse);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    void shouldReturnBadRequestWhenCourseCodeIsNotUnique() {
        when(courseService.validateEmailInstructor(validCourse.getInstructorEmail())).thenReturn(instructor);
        when(courseService.validateUniqueCodeCourse(validCourse.getCode())).thenReturn(false);

        ResponseEntity<?> response = courseController.createCourse(validCourse);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void shouldReturnBadRequestWhenCourseCodeIsInvalid() {
        validCourse.setCode("123456");
        when(courseService.validateEmailInstructor(validCourse.getInstructorEmail())).thenReturn(instructor);
        when(courseService.validateUniqueCodeCourse(validCourse.getCode())).thenReturn(true);

        ResponseEntity<?> response = courseController.createCourse(validCourse);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

}
