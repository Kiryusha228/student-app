package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class StudentInfoDto {
  Long studentProjectWorkshopId;
  Integer testResult;
  private String experience;
  private String languageProficiency;
  private String languageExperience;
  private String telegram;
  private String role;
}
