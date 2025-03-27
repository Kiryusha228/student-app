package org.example.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "student_test_result")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class StudentTestResultEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @Column(name = "test_result")
  private Integer testResult;

  @OneToOne
  @JoinColumn(name = "studentTestResult")
  private StudentProjectWorkshopEntity studentProjectWorkshop;
}
