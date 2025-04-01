package org.example.service;

import java.util.List;
import org.example.model.dto.database.StudentInTeamDto;
import org.example.model.dto.database.StudentInfoDto;
import org.example.model.entity.QuestionnaireEntity;
import org.example.model.entity.StudentProjectWorkshopEntity;
import org.example.model.entity.StudentTestResultEntity;

public interface StudentProjectWorkshopService {
  List<StudentInfoDto> getAllStudents();

  StudentProjectWorkshopEntity getStudentProjectWorkshopById(Long studentProjectWorkshopId);

  void createStudentProjectWorkshop(String studentId);

  List<StudentInTeamDto> getTeam(String studentId);
}
