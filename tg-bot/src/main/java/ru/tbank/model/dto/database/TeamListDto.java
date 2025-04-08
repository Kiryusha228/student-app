package ru.tbank.model.dto.database;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.tbank.model.dto.database.TeamDto;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TeamListDto {
    private List<TeamDto> teams;
}
