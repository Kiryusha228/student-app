package org.example.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "student_project_workshop")
public class StudentProjectWorkshopEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @ManyToOne
  @JoinColumn(name = "student_id", nullable = false)
  private StudentEntity student;

  @ManyToOne
  @JoinColumn(name = "project_workshop_id", nullable = false)
  private ProjectWorkshopEntity projectWorkshop;

  @OneToOne(mappedBy = "studentProjectWorkshop", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private QuestionnaireEntity questionnaire;

  @OneToOne(mappedBy = "studentProjectWorkshop", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private StudentTestResultEntity studentTestResult;

  @ManyToOne
  @JoinColumn(name = "team_id")
  private TeamEntity team;
}
