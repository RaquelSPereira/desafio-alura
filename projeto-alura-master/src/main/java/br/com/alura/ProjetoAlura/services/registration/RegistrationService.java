package br.com.alura.ProjetoAlura.services.registration;

import br.com.alura.ProjetoAlura.entities.course.Course;
import br.com.alura.ProjetoAlura.entities.registration.Registration;
import br.com.alura.ProjetoAlura.entities.user.User;
import br.com.alura.ProjetoAlura.models.registration.RegistrationReportItem;
import br.com.alura.ProjetoAlura.repositories.registration.RegistrationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class RegistrationService {

    @Autowired
    private RegistrationRepository registrationRepository;

    public void save (Registration registration){
        registrationRepository.save(registration);
    }

    public Optional<Registration>  findByCourseCodeAndStudentId(String courseCode, Long studentId){
        return  registrationRepository.findByCourseCodeAndStudentId(courseCode, studentId);
    }

    public Page<List<RegistrationReportItem>> findCourseRegistrationReport(Pageable pageable){
        return registrationRepository.findCourseRegistrationReport(pageable);
    }

    public Registration createRegistration(Course course, User user){
        Registration newRegistration = new Registration(
                null,
                course,
                user,
                LocalDateTime.now());
        save(newRegistration);
        newRegistration.getStudent().setPassword("**********");
        return newRegistration;
    }
}
