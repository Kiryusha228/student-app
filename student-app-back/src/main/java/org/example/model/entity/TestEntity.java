package org.example.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "test")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TestEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "test_result")
    private Integer testResult;

    @Column(name = "contest_result")
    private Integer contestResult;

    @OneToOne
    @JoinColumn(name = "student_id")
    private StudentEntity student;
}
