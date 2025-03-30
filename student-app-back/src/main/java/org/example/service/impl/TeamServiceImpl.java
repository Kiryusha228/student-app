package org.example.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.exception.ProjectWorkshopNotFoundException;
import org.example.exception.StudentProjectWorkshopNotFoundException;
import org.example.mapper.StudentMapper;
import org.example.mapper.StudentProjectWorkshopMapper;
import org.example.mapper.TeamMapper;
import org.example.model.dto.database.StudentInTeamDto;
import org.example.model.dto.database.TeamListDto;
import org.example.model.dto.database.TeamWithStudentInfoDto;
import org.example.repository.ProjectWorkshopRepository;
import org.example.repository.StudentProjectWorkshopRepository;
import org.example.repository.TeamRepository;
import org.example.service.ProjectWorkshopService;
import org.example.service.TeamService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TeamServiceImpl implements TeamService {
    private final TeamRepository teamRepository;
    private final TeamMapper teamMapper;
    private final StudentProjectWorkshopRepository studentProjectWorkshopRepository;
    private final ProjectWorkshopRepository projectWorkshopRepository;
    private final ProjectWorkshopService projectWorkshopService;
    private final StudentProjectWorkshopMapper studentProjectWorkshopMapper;

    @Override
    @Transactional
    public void createTeams(TeamListDto teamListDto) {
        var teams = teamListDto.getTeams();
        for (var team : teams) {
            var teamEntity = teamMapper.toTeamEntity(team);
            var savedTeam = teamRepository.save(teamEntity);

            for (var studentId : team.getStudents()) {
                var studentProjectWorkshop = studentProjectWorkshopRepository.findById(studentId);
                if (studentProjectWorkshop.isEmpty()) {
                    throw new StudentProjectWorkshopNotFoundException("Студент " + studentId + " не записывался на мастерскую");
                }
                studentProjectWorkshop.get().setTeam(savedTeam);
            }
        }
    }

    @Override
    public List<TeamWithStudentInfoDto> getTeams() {
        var projectWorkshop = projectWorkshopRepository.findById(projectWorkshopService.getLastProjectWorkshopId());
        if (projectWorkshop.isEmpty()) {
            throw new ProjectWorkshopNotFoundException("Мастерская не найдена");
        }
        var teams = projectWorkshop.get().getTeams();
        var teamsWithInfo = new ArrayList<TeamWithStudentInfoDto>();
        for (var team : teams) {
            var students = team.getStudentProjectWorkshop();
            var studentsInTeamWithInfo = new ArrayList<StudentInTeamDto>();
            for (var student : students) {
                studentsInTeamWithInfo.add(studentProjectWorkshopMapper.toStudentInTeamDto(student));
            }
            teamsWithInfo.add(new TeamWithStudentInfoDto(team.getId(), studentsInTeamWithInfo));
        }
        return teamsWithInfo;
    }
}
