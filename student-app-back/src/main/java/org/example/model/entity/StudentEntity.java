package org.example.model.entity;

import jakarta.persistence.*;
import java.util.List;
import lombok.*;

@Entity
@Table(name = "student")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class StudentEntity {
  @Id
  @Column(name = "id")
  private String id;

  @Column(name = "name")
  private String name;

  @Column(name = "mail")
  private String mail;

  @OneToMany(mappedBy = "student")
  private List<StudentProjectWorkshopEntity> studentProjectWorkshop;
}
