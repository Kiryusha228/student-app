package org.example.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "test")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class TestEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "test_result")
    private Integer testResult;

    @OneToOne
    @JoinColumn(name = "student_id")
    private StudentEntity student;
}
