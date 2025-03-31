package org.example.model.dto.database;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentInTeamDto {
  String name;
  String telegram;
  String role;
}
