package br.com.alura.ProjetoAlura.repository.course;

import br.com.alura.ProjetoAlura.entities.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, Long> {

    Course findByCode(String code);

    Course save(Course course);
}
