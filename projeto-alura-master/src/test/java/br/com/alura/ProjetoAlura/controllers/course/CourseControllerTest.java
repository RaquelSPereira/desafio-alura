package br.com.alura.ProjetoAlura.controllers.course;

import br.com.alura.ProjetoAlura.dtos.course.NewCourseDTO;
import br.com.alura.ProjetoAlura.entities.course.Course;
import br.com.alura.ProjetoAlura.entities.user.User;
import br.com.alura.ProjetoAlura.enums.course.CourseEnum;
import br.com.alura.ProjetoAlura.enums.role.RoleEnum;
import br.com.alura.ProjetoAlura.services.course.CourseService;
import br.com.alura.ProjetoAlura.services.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CourseControllerTest {

    @InjectMocks
    private CourseController courseController;

    @Mock
    private CourseService courseService;

    @Mock
    private UserService userService;

    private NewCourseDTO validCourse;
    private User instructor;

    @BeforeEach
    void setUp() {
        validCourse = new NewCourseDTO("Java Course", "JAVA-AD", "Advanced Java", "instructor@mail.com");
        instructor = new User("Instructor Name", "instructor@mail.com", RoleEnum.INSTRUCTOR, "password");
    }

    @Test
    void shouldCreateCourseSuccessfully() {
        when(userService.findByEmail(validCourse.getInstructorEmail())).thenReturn(instructor);
        when(courseService.validateUniqueCodeCourse(validCourse.getCode())).thenReturn(true);
        when(courseService.save(any(Course.class))).thenReturn(new Course());

        ResponseEntity<?> response = courseController.createCourse(validCourse);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    void shouldReturnUnauthorizedWhenInstructorIsStudent() {
        instructor.setRole(RoleEnum.STUDENT);
        when(userService.findByEmail(validCourse.getInstructorEmail())).thenReturn(instructor);

        ResponseEntity<?> response = courseController.createCourse(validCourse);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    void shouldReturnBadRequestWhenCourseCodeIsNotUnique() {
        when(userService.findByEmail(validCourse.getInstructorEmail())).thenReturn(instructor);
        when(courseService.validateUniqueCodeCourse(validCourse.getCode())).thenReturn(false);

        ResponseEntity<?> response = courseController.createCourse(validCourse);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void shouldReturnBadRequestWhenCourseCodeIsInvalid() {
        validCourse.setCode("123456");
        when(userService.findByEmail(validCourse.getInstructorEmail())).thenReturn(instructor);
        when(courseService.validateUniqueCodeCourse(validCourse.getCode())).thenReturn(true);

        ResponseEntity<?> response = courseController.createCourse(validCourse);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }


    @Test
    void shouldInactivateCourseSuccessfully() {
        String courseCode = "JAVA-AD";
        String instructorEmail = "instructor@mail.com";
        User instructor = new User("Instructor Name", "instructor@mail.com", RoleEnum.INSTRUCTOR, "password");
        Course course = new Course();
        course.setCode(courseCode);
        course.setStatus(CourseEnum.ACTIVE);

        when(userService.findByEmail(instructorEmail)).thenReturn(instructor);
        when(courseService.findByCode(courseCode)).thenReturn(course);
        when(courseService.save(any(Course.class))).thenReturn(course);

        ResponseEntity<?> response = courseController.inactivateCourse(courseCode, instructorEmail);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(CourseEnum.INACTIVE, course.getStatus());
    }

    @Test
    void shouldReturnUnauthorizedWhenInstructorIsNotValid() {
        String courseCode = "JAVA-AD";
        String instructorEmail = "instructor@mail.com";
        User student = new User("Student Name", "student@mail.com", RoleEnum.STUDENT, "password");

        when(userService.findByEmail(instructorEmail)).thenReturn(student);

        ResponseEntity<?> response = courseController.inactivateCourse(courseCode, instructorEmail);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    void shouldReturnBadRequestWhenCourseNotFound() {
        String courseCode = "JAVA-AD";
        String instructorEmail = "instructor@mail.com";
        User instructor = new User("Instructor Name", "instructor@mail.com", RoleEnum.INSTRUCTOR, "password");

        when(userService.findByEmail(instructorEmail)).thenReturn(instructor);
        when(courseService.findByCode(courseCode)).thenReturn(null);  // Curso não encontrado

        ResponseEntity<?> response = courseController.inactivateCourse(courseCode, instructorEmail);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void shouldReturnBadRequestWhenCourseIsAlreadyInactive() {
        String courseCode = "JAVA-AD";
        String instructorEmail = "instructor@mail.com";
        User instructor = new User("Instructor Name", "instructor@mail.com", RoleEnum.INSTRUCTOR, "password");
        Course course = new Course();
        course.setCode(courseCode);
        course.setStatus(CourseEnum.INACTIVE);

        when(userService.findByEmail(instructorEmail)).thenReturn(instructor);
        when(courseService.findByCode(courseCode)).thenReturn(course);

        ResponseEntity<?> response = courseController.inactivateCourse(courseCode, instructorEmail);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }


}
