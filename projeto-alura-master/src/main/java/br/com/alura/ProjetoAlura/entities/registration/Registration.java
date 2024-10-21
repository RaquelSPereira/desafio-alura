package br.com.alura.ProjetoAlura.entities.registration;

import br.com.alura.ProjetoAlura.entities.user.User;
import br.com.alura.ProjetoAlura.entities.course.Course;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Registration")
public class Registration{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "courseId", nullable = false)
    private Course course;

    @ManyToOne
    @JoinColumn(name = "studentId", nullable = false)
    private User student;

    @Column(name = "registration_date", nullable = false)
    private LocalDateTime registrationDate;
}
