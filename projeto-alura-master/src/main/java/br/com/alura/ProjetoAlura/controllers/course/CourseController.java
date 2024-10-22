package br.com.alura.ProjetoAlura.controllers.course;

import br.com.alura.ProjetoAlura.dtos.course.NewCourseDTO;
import br.com.alura.ProjetoAlura.entities.course.Course;
import br.com.alura.ProjetoAlura.entities.user.User;
import br.com.alura.ProjetoAlura.enums.course.CourseEnum;
import br.com.alura.ProjetoAlura.enums.role.RoleEnum;
import br.com.alura.ProjetoAlura.services.course.CourseService;
import br.com.alura.ProjetoAlura.services.user.UserService;
import br.com.alura.ProjetoAlura.util.exceptions.NotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

import static br.com.alura.ProjetoAlura.services.course.CourseService.isValidCourseCode;

@RestController
public class CourseController {

    @Autowired
    private CourseService courseService;

    @Autowired
    private UserService userService;

    @PostMapping("/course/new")
    public ResponseEntity createCourse(@Valid @RequestBody NewCourseDTO newCourse) {
        User instructor = userService.findByEmail(newCourse.getInstructorEmail());
//        if (instructor.getRole().equals(RoleEnum.STUDENT)){
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
//        }
        boolean isUniqueCodeCourse = courseService.validateUniqueCodeCourse(newCourse.getCode());
        if (!isUniqueCodeCourse){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        boolean isValideCourseCode = isValidCourseCode(newCourse.getCode());

        if (!isValideCourseCode){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        Course course = new Course();
        course.setInstructor(instructor);
        BeanUtils.copyProperties(newCourse, course);
        courseService.save(course);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    //colocar validacao no path variable
    @PatchMapping("/course/inactive/{code}")
    public ResponseEntity inactivateCourse(@PathVariable("code") String courseCode) {
        Course course = courseService.findByCode(courseCode);
        if (course == null){
            throw new NotFoundException("Código do curso inválido");
        }

        if (course.getStatus().equals(CourseEnum.INACTIVE)){
            throw new NotFoundException("O curso já está inativo");
        }
        course.setStatus(CourseEnum.INACTIVE);
        course.setInactivationDate(LocalDateTime.now());
        courseService.save(course);

        return ResponseEntity.ok().build();
    }

}
