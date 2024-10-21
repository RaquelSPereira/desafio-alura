package br.com.alura.ProjetoAlura.controllers.registration;

import br.com.alura.ProjetoAlura.dtos.registration.NewRegistrationDTO;
import br.com.alura.ProjetoAlura.entities.course.Course;
import br.com.alura.ProjetoAlura.entities.registration.Registration;
import br.com.alura.ProjetoAlura.entities.user.User;
import br.com.alura.ProjetoAlura.enums.course.CourseEnum;
import br.com.alura.ProjetoAlura.enums.role.RoleEnum;
import br.com.alura.ProjetoAlura.models.registration.RegistrationReportItem;
import br.com.alura.ProjetoAlura.services.course.CourseService;
import br.com.alura.ProjetoAlura.services.registration.RegistrationService;
import br.com.alura.ProjetoAlura.services.user.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
public class RegistrationController {

    @Autowired
    private CourseService courseService;

    @Autowired
    private UserService userService;

    @Autowired
    private RegistrationService registrationService;

    @PostMapping("/registration/new")
    public ResponseEntity newRegistration (@Valid @RequestBody NewRegistrationDTO newRegistration) {
        Course course = courseService.findByCode(newRegistration.getCourseCode());
        User user = userService.findByEmail(newRegistration.getStudentEmail());

        if (course == null || course.getStatus().equals(CourseEnum.INACTIVE) || user == null || !user.getRole().equals(RoleEnum.STUDENT)){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        Registration registration = registrationService.findByCourseCodeAndStudentId(course.getCode(), user.getId());
        if(registration != null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

        }
        Registration newRegistrationStudent = new Registration(null, course, user, LocalDateTime.now());
        registrationService.save(newRegistrationStudent);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/registration/report")
    public ResponseEntity<List<RegistrationReportItem>> report() {
        List<RegistrationReportItem> items = registrationService.findCourseRegistrationReport();
        return ResponseEntity.ok(items);
    }

}
