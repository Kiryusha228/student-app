package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.dto.TeamDto;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TeamListDto {
  private List<TeamDto> teams;
}
