package org.example.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.mapper.TeamMapper;
import org.example.model.dto.database.TeamDto;
import org.example.model.dto.database.TeamListDto;
import org.example.model.entity.TeamEntity;
import org.example.repository.TeamRepository;
import org.example.service.TeamService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TeamServiceImpl implements TeamService {
    private final TeamRepository teamRepository;
    private final TeamMapper teamMapper;

    @Override
    public void createTeams(TeamListDto teamListDto) {
        var teams = teamListDto.getTeams();
        for (var team : teams) {
            teamRepository.save(teamMapper.toTeamEntity(team));
        }
    }
}
