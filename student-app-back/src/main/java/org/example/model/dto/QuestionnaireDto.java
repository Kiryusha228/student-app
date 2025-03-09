package org.example.model.dto;


import lombok.*;

@AllArgsConstructor
@Data
public class QuestionnaireDto {
    private Long id;
    private String university;
    private Integer graduationYear;
    private String faculty;
    private String experience;
    private String languageProficiency;
    private String languageExperience;
    private String telegram;
    private String role;
    private String github;
    private String studentId;
}
