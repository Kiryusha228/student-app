package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class QuestionnaireDto {
  private String university;
  private Integer graduationYear;
  private String faculty;
  private String experience;
  private String languageProficiency;
  private String languageExperience;
  private String telegram;
  private String role;
  private String github;
}
