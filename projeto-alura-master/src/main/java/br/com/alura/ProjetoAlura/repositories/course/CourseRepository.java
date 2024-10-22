package br.com.alura.ProjetoAlura.repositories.course;

import br.com.alura.ProjetoAlura.entities.course.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    Optional<Course> findByCode(String code);

}
