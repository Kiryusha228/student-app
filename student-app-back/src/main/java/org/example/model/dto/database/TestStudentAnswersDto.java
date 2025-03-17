package org.example.model.dto.database;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class TestStudentAnswersDto {
  List<Integer> answers;
}
