package br.com.alura.ProjetoAlura.repository.registration;

import br.com.alura.ProjetoAlura.entities.Registration;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RegistrationRepository  extends JpaRepository<Registration, Long> {

    Registration save (Registration registration);

    Registration findByCourseCodeAndStudentId(String courseCode, Long studentId);
}
