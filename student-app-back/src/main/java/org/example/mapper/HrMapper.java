package org.example.mapper;

import org.example.model.entity.HrEntity;
import org.springframework.stereotype.Component;

@Component
public class HrMapper {
    public HrEntity telegramChatIdToHrEntity(Long telegramChatId){
        return new HrEntity(telegramChatId);
    }

}
