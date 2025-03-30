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
@Table(name = "team")
public class TeamEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @OneToMany(mappedBy = "team")
  private List<StudentProjectWorkshopEntity> studentProjectWorkshop;

  @ManyToOne
  @JoinColumn(name = "project_workshop_id", nullable = false)
  private ProjectWorkshopEntity projectWorkshop;
}
