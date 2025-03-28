package org.example.model.dto.database;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.model.entity.TeamEntity;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TeamListDto {
    private List<TeamEntity> teams;
}
