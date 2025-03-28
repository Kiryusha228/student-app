package org.example.service;

import org.example.model.dto.database.TeamDto;
import org.example.model.dto.database.TeamListDto;
import org.example.model.entity.TeamEntity;

public interface TeamService {
    void createTeams(TeamListDto teamListDto);
}
