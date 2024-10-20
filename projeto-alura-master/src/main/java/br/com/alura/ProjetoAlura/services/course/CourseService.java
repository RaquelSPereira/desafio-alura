package br.com.alura.ProjetoAlura.services.course;

import br.com.alura.ProjetoAlura.entities.Course;
import br.com.alura.ProjetoAlura.repository.course.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class CourseService {

    @Autowired
    private CourseRepository courseRepository;

    public Course save (Course course){
        return courseRepository.save(course);
    }

    public  Course findByCode (String code){
        return  courseRepository.findByCode(code);
    }

    public boolean validateUniqueCodeCourse(String codeCourse){
        Course course = courseRepository.findByCode(codeCourse);
        return course == null;
    }

    public static boolean isValidCourseCode(String name) {
        String regex = "^[a-zA-Z]+(-[a-zA-Z]+)*$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(name);

        return matcher.matches();
    }
}
