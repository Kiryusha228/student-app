package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.model.dto.database.TeamWithStudentInfoDto;
import org.example.service.TeamService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/team")
public class TeamController {
    private final TeamService teamService;

    @GetMapping("/get")
    public List<TeamWithStudentInfoDto> getTest() {
        return teamService.getTeams();
    }
}
