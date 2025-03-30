package org.example.model.entity;

import jakarta.persistence.*;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "project_workshop")
public class ProjectWorkshopEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @Column(name = "name")
  private String name;

  @Column(name = "year")
  private Integer year;

  @OneToMany(mappedBy = "projectWorkshop")
  private List<StudentProjectWorkshopEntity> studentProjectWorkshop;

  @OneToMany(mappedBy = "projectWorkshop")
  private List<TeamEntity> teams;
}
