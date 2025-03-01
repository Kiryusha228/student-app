package org.example.model.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
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
    private Long studentId;
}
