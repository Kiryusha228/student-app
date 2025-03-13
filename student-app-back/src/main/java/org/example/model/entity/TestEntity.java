package org.example.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

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

    @Column(name = "question")
    private String testQuestion;

    @Column(name = "answers")
    private List<String> answers;

    @Column(name = "right_answer")
    private Integer rightAnswer;
}
