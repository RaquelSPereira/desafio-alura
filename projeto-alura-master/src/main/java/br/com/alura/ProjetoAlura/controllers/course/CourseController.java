package br.com.alura.ProjetoAlura.controllers.course;

import br.com.alura.ProjetoAlura.dtos.course.NewCourseDTO;
import br.com.alura.ProjetoAlura.entities.Course;
import br.com.alura.ProjetoAlura.entities.User;
import br.com.alura.ProjetoAlura.enums.course.CourseEnum;
import br.com.alura.ProjetoAlura.enums.role.RoleEnum;
import br.com.alura.ProjetoAlura.services.course.CourseService;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static br.com.alura.ProjetoAlura.services.course.CourseService.isValidCourseCode;

@RestController
public class CourseController {

    @Autowired
    private CourseService courseService;

    @PostMapping("/course/new")
    public ResponseEntity createCourse(@Valid @RequestBody NewCourseDTO newCourse) {
        User instructor = courseService.validateEmailInstructor(newCourse.getInstructorEmail());
        if (instructor.getRole().equals(RoleEnum.STUDENT)){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
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

    @PostMapping("/course/{code}/inactive")
    public ResponseEntity createCourse(@PathVariable("code") String courseCode) {
        // TODO: Implementar a Questão 2 - Inativação de Curso aqui...

        return ResponseEntity.ok().build();
    }

}
