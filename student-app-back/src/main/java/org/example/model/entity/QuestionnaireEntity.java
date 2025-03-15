package org.example.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "questionnaire")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class QuestionnaireEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @Column(name = "university")
  private String university;

  @Column(name = "graduation_year")
  private Integer graduationYear;

  @Column(name = "faculty")
  private String faculty;

  @Column(name = "experience")
  private String experience;

  @Column(name = "language_proficiency")
  private String languageProficiency;

  @Column(name = "language_experience")
  private String languageExperience;

  @Column(name = "telegram")
  private String telegram;

  @Column(name = "role")
  private String role;

  @Column(name = "github")
  private String github;

  @OneToOne
  @JoinColumn(name = "student_id")
  private StudentEntity student;
}
