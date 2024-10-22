package br.com.alura.ProjetoAlura.services.course;

import br.com.alura.ProjetoAlura.entities.course.Course;
import br.com.alura.ProjetoAlura.repositories.course.CourseRepository;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class CourseServiceTest {

    @Mock
    private CourseRepository courseRepository;

    @InjectMocks
    private CourseService courseService;

    private Course course;

    @BeforeEach
    public void setUp() {
        course = new Course();
        course.setCode("CS101");
        course.setName("Computer Science");
    }

    @Test
    public void shouldCallRepositorySaveMethodWithSameCourse() {
        when(courseRepository.save(course)).thenReturn(course);
        courseService.save(course);
        verify(courseRepository, times(1)).save(course);
    }

    @Test
    public void shouldReturnCourseWhenFoundByCode() {
        when(courseRepository.findByCode("CS101")).thenReturn(Optional.of(course));
        Course foundCourse = courseService.findByCode("CS101").get();
        assertEquals(course.getCode(), foundCourse.getCode());
    }

    @Test
    public void shouldCallFindByCodeMethodOfRepositoryWithCorrectParameter() {
        when(courseRepository.findByCode("CS101")).thenReturn(Optional.of(course));
        courseService.findByCode("CS101");
        verify(courseRepository, times(1)).findByCode("CS101");
    }

    @Test
    public void shouldReturnTrueIfCourseCodeIsUnique() {
        when(courseRepository.findByCode("CS101")).thenReturn(Optional.empty());
        boolean isUnique = courseService.validateUniqueCodeCourse("CS101");
        assertTrue(isUnique);
    }

    @Test
    public void shouldReturnFalseIfCourseCodeIsNotUnique() {
        when(courseRepository.findByCode("CS101")).thenReturn(Optional.ofNullable(course));
        boolean isUnique = courseService.validateUniqueCodeCourse("CS101");
        assertFalse(isUnique);
    }

    @Test
    public void shouldCallValidateUniqueCodeCourseOfRepositoryWithCorrectParameter() {
        when(courseRepository.findByCode("CS101")).thenReturn(Optional.empty());
        courseService.validateUniqueCodeCourse("CS101");
        verify(courseRepository, times(1)).findByCode("CS101");
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "CS 101",          // Espaços
            "CS - Web",        // Espaços com hífen
            "CS  Web",         // Espaço no meio
            "123",             // Apenas números
            "CS101",           // Letras e números sem hífen
            "C1S",             // Letra e número misturados
            "CS1",             // Mistura de número e letras sem hífen
            "CS@101",          // Caractere especial `@`
            "CS#123",          // Caractere especial `#`
            "C$S123",          // Caractere especial `$`
            "C%S-Web",         // Caractere especial `%`
            "CS_101",          // Sublinhado `_`
            "CS&Web",          // Caractere especial `&`
            "CS+123",          // Caractere especial `+`
            "CS!@#",           // Vários caracteres especiais
            "CS?Web",          // Caractere especial `?`
            "C$S -123",        // Caractere especial `$` e números
            "123 CS-",         // Números seguidos de letras e hífen
            "CSWeb&123",       // Caractere especial `&` misturado com números
            "CSWeb_ ",         // Caractere especial `_` e espaço
            "CS--Web",         // Hífens consecutivos
            "-CSWeb",          // Hífen no início
            "CSWeb-",          // Hífen no final
            "CSWEB123",        // Letras e números misturados
            "CSWeb_123",       // Caractere especial `_` misturado com números
            "CS-Web@",         // Caractere especial `@` misturado com hífen
            "CS@-Web",         // Caractere especial `@` misturado com hífen
            "C-SWeb@",         // Hífen e caractere especial `@`
            "Web-1",           // Mistura de letras e números sem hífen
            "Web@-01"          // Mistura de caracteres especiais, números e hífen
    })
    public void shouldReturnFalseIfCourseCodeIsInvalid(String courseCode) {

        boolean isValid = CourseService.isValidCourseCode(courseCode);
        assertFalse(isValid);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "CSWEB-JAVA",          // Espaços
            "CSWEBJAVA",        // Espaços com hífen
            "CS-WEB-JAV",         // Espaço no meio
    })
    public void shouldReturnTrueIfCourseCodeIsValid(String courseCode) {
        boolean isValid = CourseService.isValidCourseCode(courseCode);
        assertTrue(isValid);
    }
}
