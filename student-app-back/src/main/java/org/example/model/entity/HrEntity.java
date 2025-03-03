package org.example.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "hr")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class HrEntity {
    @Id
    @Column(name = "telegram_chat_id")
    private Long telegramChatId;
}
