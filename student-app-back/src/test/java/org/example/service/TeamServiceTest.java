package org.example.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import org.example.exception.StudentProjectWorkshopNotFoundException;
import org.example.mapper.TeamMapper;
import org.example.model.dto.database.TeamDto;
import org.example.model.dto.database.TeamListDto;
import org.example.model.entity.StudentProjectWorkshopEntity;
import org.example.model.entity.TeamEntity;
import org.example.repository.StudentProjectWorkshopRepository;
import org.example.repository.TeamRepository;
import org.example.service.impl.TeamServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TeamServiceTest {
  @Mock private TeamRepository teamRepository;
  @Mock private TeamMapper teamMapper;
  @Mock private StudentProjectWorkshopRepository studentProjectWorkshopRepository;

  @InjectMocks private TeamServiceImpl teamService;

  @Test
  void createTeams() {
    // Arrange
    var student1 = new StudentProjectWorkshopEntity();
    student1.setId(1L);
    var student2 = new StudentProjectWorkshopEntity();
    student2.setId(2L);

    var team = new TeamDto(List.of(1L, 2L));

    var teamEntity = new TeamEntity();
    teamEntity.setId(1L);
    teamEntity.setStudentProjectWorkshop(List.of(student1, student2));

    var teamList = new TeamListDto(List.of(team));

    when(teamMapper.toTeamEntity(team)).thenReturn(teamEntity);
    when(teamRepository.save(teamEntity)).thenReturn(teamEntity);
    when(studentProjectWorkshopRepository.findById(student1.getId()))
        .thenReturn(Optional.of(student1));
    when(studentProjectWorkshopRepository.findById(student2.getId()))
        .thenReturn(Optional.of(student2));

    // Act
    teamService.createTeams(teamList);

    // Assert
    verify(teamRepository).save(teamEntity);
    assertEquals(student1.getTeam(), teamEntity);
    assertEquals(student2.getTeam(), teamEntity);
  }

  @Test
  void createTeamsWithStudentProjectWorkshopNotFoundException() {
    // Arrange
    var student1 = new StudentProjectWorkshopEntity();
    student1.setId(1L);
    var student2 = new StudentProjectWorkshopEntity();
    student2.setId(2L);

    var team = new TeamDto(List.of(1L, 2L));

    var teamEntity = new TeamEntity();
    teamEntity.setId(1L);
    teamEntity.setStudentProjectWorkshop(List.of(student1, student2));

    var teamList = new TeamListDto(List.of(team));

    when(teamMapper.toTeamEntity(team)).thenReturn(teamEntity);
    when(teamRepository.save(teamEntity)).thenReturn(teamEntity);
    when(studentProjectWorkshopRepository.findById(student1.getId())).thenReturn(Optional.empty());

    // Act & Assert
    assertThrows(
        StudentProjectWorkshopNotFoundException.class, () -> teamService.createTeams(teamList));
  }
}
