package org.example.service;

import org.example.model.dto.database.TeamListDto;
import org.example.model.dto.database.TeamWithStudentInfoDto;

import java.util.List;

public interface TeamService {
    void createTeams(TeamListDto teamListDto);

    List<TeamWithStudentInfoDto> getTeams();
}
