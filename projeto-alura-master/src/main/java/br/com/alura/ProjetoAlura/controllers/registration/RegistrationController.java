package br.com.alura.ProjetoAlura.controllers.registration;

import br.com.alura.ProjetoAlura.dtos.registration.NewRegistrationDTO;
import br.com.alura.ProjetoAlura.dtos.response.SuccessResponseDTO;
import br.com.alura.ProjetoAlura.entities.course.Course;
import br.com.alura.ProjetoAlura.entities.registration.Registration;
import br.com.alura.ProjetoAlura.entities.user.User;
import br.com.alura.ProjetoAlura.enums.course.CourseEnum;
import br.com.alura.ProjetoAlura.enums.role.RoleEnum;
import br.com.alura.ProjetoAlura.models.registration.RegistrationReportItem;
import br.com.alura.ProjetoAlura.services.course.CourseService;
import br.com.alura.ProjetoAlura.services.registration.RegistrationService;
import br.com.alura.ProjetoAlura.services.user.UserService;
import br.com.alura.ProjetoAlura.util.exceptions.BadRequestException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class RegistrationController {

    @Autowired
    private CourseService courseService;

    @Autowired
    private UserService userService;

    @Autowired
    private RegistrationService registrationService;

    @PostMapping("/registration/new")
    public ResponseEntity<SuccessResponseDTO<Registration>> newRegistration (@Valid @RequestBody NewRegistrationDTO newRegistration) {
        Optional<Course> course = courseService.findByCode(newRegistration.getCourseCode());
        Optional<User> user = userService.findByEmail(newRegistration.getStudentEmail());

        if (course.isEmpty()){
            throw new BadRequestException("Código do curso inválido");
        }

        if (course.get().getStatus().equals(CourseEnum.INACTIVE)){
            throw new BadRequestException("Curso inativado");
        }

        if(user.isEmpty()){
            throw new BadRequestException("Usuário não encontrado");
        }

        if(user.get().getRole().equals(RoleEnum.INSTRUCTOR)){
            throw new BadRequestException("Email informado inválido");
        }

        if(registrationService.findByCourseCodeAndStudentId(course.get().getCode(), user.get().getId()).isPresent()){
            throw new BadRequestException("Aluno já cadastrado no curso");
        }

        Registration response = registrationService.createRegistration(course.get(), user.get());
        return new ResponseEntity<>(new SuccessResponseDTO<>("Matrícula realizada com sucesso!", response), HttpStatus.CREATED);
    }


    @GetMapping("/registration/report")
    public ResponseEntity<Page<List<RegistrationReportItem>>> report(
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<List<RegistrationReportItem>> items = registrationService.findCourseRegistrationReport(pageable);
        return ResponseEntity.ok(items);
    }

}
