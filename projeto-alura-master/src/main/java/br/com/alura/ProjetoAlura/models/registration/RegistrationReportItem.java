package br.com.alura.ProjetoAlura.models.registration;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class RegistrationReportItem {

    private final String courseName;
    private final String courseCode;
    private final String instructorName;
    private final String instructorEmail;
    private final Long totalRegistrations;

    public RegistrationReportItem(String courseName, String courseCode, String instructorName, String instructorEmail, Long totalRegistrations) {
        this.courseName = courseName;
        this.courseCode = courseCode;
        this.instructorName = instructorName;
        this.instructorEmail = instructorEmail;
        this.totalRegistrations = totalRegistrations;
    }

}
