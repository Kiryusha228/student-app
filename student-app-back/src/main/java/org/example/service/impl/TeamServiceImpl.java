package org.example.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.exception.ProjectWorkshopEnabledException;
import org.example.exception.StudentProjectWorkshopNotFoundException;
import org.example.mapper.TeamMapper;
import org.example.model.dto.database.TeamListDto;
import org.example.repository.StudentProjectWorkshopRepository;
import org.example.repository.TeamRepository;
import org.example.service.ProjectWorkshopService;
import org.example.service.TeamService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TeamServiceImpl implements TeamService {
  private final TeamRepository teamRepository;
  private final TeamMapper teamMapper;
  private final StudentProjectWorkshopRepository studentProjectWorkshopRepository;
  private final ProjectWorkshopService projectWorkshopService;

  @Override
  @Transactional
  public void createTeams(TeamListDto teamListDto) {
    if (projectWorkshopService.getLastProjectWorkshop().getIsEnable()) {
      throw new ProjectWorkshopEnabledException("Набор на последнюю мастерскую еще открыт");
    }

    var teams = teamListDto.getTeams();
    for (var team : teams) {
      var teamEntity = teamMapper.toTeamEntity(team);
      var savedTeam = teamRepository.save(teamEntity);

      for (var studentId : team.getStudents()) {
        var studentProjectWorkshop = studentProjectWorkshopRepository.findById(studentId);
        if (studentProjectWorkshop.isEmpty()) {
          throw new StudentProjectWorkshopNotFoundException(
              "Студент " + studentId + " не записывался на мастерскую");
        }
        studentProjectWorkshop.get().setTeam(savedTeam);
      }
    }
  }
}
