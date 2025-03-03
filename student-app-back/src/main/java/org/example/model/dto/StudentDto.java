package org.example.model.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class StudentDto {
    private Long id;
    private String name;
    private String password;
    private String mail;
}
