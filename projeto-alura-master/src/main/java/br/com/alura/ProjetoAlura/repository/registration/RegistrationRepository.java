package br.com.alura.ProjetoAlura.repository.registration;

import br.com.alura.ProjetoAlura.entities.registration.Registration;
import br.com.alura.ProjetoAlura.models.registration.RegistrationReportItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RegistrationRepository  extends JpaRepository<Registration, Long> {

    Registration save(Registration registration);

    Registration findByCourseCodeAndStudentId(String courseCode, Long studentId);

    @Query(
            value = "SELECT" +
                    " new br.com.alura.ProjetoAlura.models.registration.RegistrationReportItem( " +
                    " c.name AS courseName, " +
                    " c.code AS courseCode, " +
                    " u.name AS instructorName, " +
                    " u.email AS instructorEmail, " +
                    " COUNT(r.id) AS totalRegistrations " +
                    ") " +
                    "FROM " +
                    "    Course c " +
                    "JOIN " +
                    "    Registration r ON r.course.id = c.id " +
                    "JOIN " +
                    "    User u ON c.instructor.id = u.id " +
                    "WHERE " +
                    "    u.role = 'INSTRUCTOR' " +
                    "GROUP BY " +
                    "    c.id, c.name, c.code, u.name, u.email " +
                    "ORDER BY " +
                    "    totalRegistrations DESC"
    )
    List<RegistrationReportItem> findCourseRegistrationReport();

}

