package org.example.mapper;

import lombok.RequiredArgsConstructor;
import org.example.model.dto.database.TeamDto;
import org.example.model.entity.StudentProjectWorkshopEntity;
import org.example.model.entity.TeamEntity;
import org.example.service.StudentProjectWorkshopService;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
@RequiredArgsConstructor
public class TeamMapper {
    private final StudentProjectWorkshopService studentProjectWorkshopService;

    public TeamEntity toTeamEntity(TeamDto teamDto) {
        var studentsProjectWorkshop = new ArrayList<StudentProjectWorkshopEntity>();
        var students = teamDto.getStudents();
        for (var studentId : students) {
            studentsProjectWorkshop.add(studentProjectWorkshopService.getStudentProjectWorkshopById(studentId));
        }

        return new TeamEntity(
                0L,
                studentsProjectWorkshop,
                studentsProjectWorkshop.get(0).getProjectWorkshop()
        );
    }
}
