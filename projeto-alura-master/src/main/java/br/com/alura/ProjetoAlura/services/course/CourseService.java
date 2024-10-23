package br.com.alura.ProjetoAlura.services.course;

import br.com.alura.ProjetoAlura.dtos.course.NewCourseDTO;
import br.com.alura.ProjetoAlura.entities.course.Course;
import br.com.alura.ProjetoAlura.entities.user.User;
import br.com.alura.ProjetoAlura.enums.course.CourseEnum;
import br.com.alura.ProjetoAlura.repositories.course.CourseRepository;
import br.com.alura.ProjetoAlura.services.user.UserService;
import br.com.alura.ProjetoAlura.util.exceptions.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class CourseService {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private UserService userService;

    public void save (Course course){
         courseRepository.save(course);
    }

    public Optional<Course> findByCode (String code){
        return courseRepository.findByCode(code);
    }

    public boolean validateUniqueCodeCourse(String codeCourse){
        return courseRepository.findByCode(codeCourse).isEmpty();
    }

    public static boolean isValidCourseCode(String name) {
        String regex = "^[a-zA-Z]+(-[a-zA-Z]+)*$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(name);

        return matcher.matches();
    }

    public boolean isValidUpdateInactiveStatusInCourse(String code){
        return Optional.ofNullable(findByCode(code))
                .filter(course -> !course.get().getStatus().equals(CourseEnum.INACTIVE)).isPresent();
    }

    public Course updateInactiveStatusInCourse(String code){
        Course course = findByCode(code).orElseThrow(() -> new NotFoundException("Curso não encontrado."));;
        course.setStatus(CourseEnum.INACTIVE);
        course.setInactivationDate(LocalDateTime.now());
        save(course);
        return course;
    }

    public Course createCourse(NewCourseDTO newCourseDTO){
        Optional<User> user = userService.findByEmail(newCourseDTO.getInstructorEmail());
        Course course = new Course(
                null,
                newCourseDTO.getName(),
                newCourseDTO.getCode(),
                user.get(),
                newCourseDTO.getDescription(),
                CourseEnum.ACTIVE,
                null,
                LocalDateTime.now());
        save(course);
        return course;

    }
}
