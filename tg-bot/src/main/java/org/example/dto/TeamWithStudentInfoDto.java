package org.example.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TeamWithStudentInfoDto {
  Long id;
  List<StudentInTeamDto> students;
}
