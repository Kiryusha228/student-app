package ru.tbank.model.dto.database;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.tbank.model.dto.database.StudentInTeamDto;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TeamWithStudentInfoDto {
    Long id;
    List<StudentInTeamDto> students;
}