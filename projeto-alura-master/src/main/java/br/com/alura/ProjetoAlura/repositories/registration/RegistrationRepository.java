package br.com.alura.ProjetoAlura.repositories.registration;

import br.com.alura.ProjetoAlura.entities.registration.Registration;
import br.com.alura.ProjetoAlura.models.registration.RegistrationReportItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RegistrationRepository  extends JpaRepository<Registration, Long> {

    Optional<Registration>  findByCourseCodeAndStudentId(String courseCode, Long studentId);

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
    Page<List<RegistrationReportItem>> findCourseRegistrationReport(Pageable pageable);

}

