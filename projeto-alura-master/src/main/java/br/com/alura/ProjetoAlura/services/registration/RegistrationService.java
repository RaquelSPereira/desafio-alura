package br.com.alura.ProjetoAlura.services.registration;

import br.com.alura.ProjetoAlura.entities.Registration;
import br.com.alura.ProjetoAlura.repository.registration.RegistrationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RegistrationService {

    @Autowired
    private RegistrationRepository registrationRepository;

    public Registration save (Registration registration){
        return  registrationRepository.save(registration);
    }

    public Registration findByCourseCodeAndStudentId(String courseCode, Long studentId){
        return  registrationRepository.findByCourseCodeAndStudentId(courseCode, studentId);
    }
}
