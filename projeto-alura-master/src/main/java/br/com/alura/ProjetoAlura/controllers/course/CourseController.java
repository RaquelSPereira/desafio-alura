package br.com.alura.ProjetoAlura.controllers.course;

import br.com.alura.ProjetoAlura.dtos.course.NewCourseDTO;
import br.com.alura.ProjetoAlura.dtos.response.SuccessResponseDTO;
import br.com.alura.ProjetoAlura.entities.course.Course;
import br.com.alura.ProjetoAlura.services.course.CourseService;
import br.com.alura.ProjetoAlura.services.user.UserService;
import br.com.alura.ProjetoAlura.util.exceptions.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static br.com.alura.ProjetoAlura.services.course.CourseService.isValidCourseCode;

@RestController
public class CourseController {

    @Autowired
    private CourseService courseService;

    @Autowired
    private UserService userService;

    @PostMapping("/course/new")
    public ResponseEntity<SuccessResponseDTO<Course>> createCourse(@Valid @RequestBody NewCourseDTO newCourse) {

        if (!userService.isInstructor(newCourse.getInstructorEmail())){
            throw new NotFoundException("Email informado não pertence a um instrutor");
        }

        if (!courseService.validateUniqueCodeCourse(newCourse.getCode())){
            throw new BadRequestException("Código do curso já existe");
        }

        if (!isValidCourseCode(newCourse.getCode())){
            throw new BadRequestException("Código do curso inválido");

        }
        Course response = courseService.createCourse(newCourse);
        return new ResponseEntity<>(new SuccessResponseDTO<>("Curso criado com sucesso!", response), HttpStatus.CREATED);
    }


    @PatchMapping("/course/inactive/{code}")
    public ResponseEntity<SuccessResponseDTO<Course>> inactivateCourse(@NotBlank @NotNull @PathVariable("code") String courseCode) {

        if(!courseService.isValidUpdateInactiveStatusInCourse(courseCode)){
            throw new BadRequestException("Não é possível alterar status do curso");
        }
        Course response = courseService.updateInactiveStatusInCourse(courseCode);
        return new ResponseEntity<>(new SuccessResponseDTO<>("Curso atualizado com sucesso!", response), HttpStatus.OK);
    }

}
