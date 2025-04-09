package org.example.service;

import java.util.List;
import org.example.model.dto.database.StudentInTeamDto;
import org.example.model.dto.database.StudentInfoDto;
import org.example.model.entity.StudentProjectWorkshopEntity;

public interface StudentProjectWorkshopService {
  List<StudentInfoDto> getAllPastStudents();

  List<StudentInfoDto> getAllStudentsByProjectWorkshopId(Long projectWorkshopId);

  StudentProjectWorkshopEntity getStudentProjectWorkshopById(Long studentProjectWorkshopId);

  void createStudentProjectWorkshop(String studentId);

  List<StudentInTeamDto> getTeam(String studentId);

  StudentInfoDto getStudentInfoById(Long id);

  StudentInfoDto getStudentInfoByTelegram(String telegram);

  StudentInfoDto getStudentInfoByName(String name);

  List<StudentInfoDto> getStudentsInfoByName(String name);

  Boolean checkStudentRegistrationOnProjectWorkshop(String studentId, Long projectWorkshopId);
}
