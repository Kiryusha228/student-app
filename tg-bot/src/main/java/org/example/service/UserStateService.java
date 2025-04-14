package org.example.service;

import org.example.enums.BotState;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserStateService {
    private final Map<Long, BotState> userStates = new HashMap<>();

    public void setState(long chatId, BotState state) {
        userStates.put(chatId, state);
    }

    public BotState getState(long chatId) {
        return userStates.getOrDefault(chatId, BotState.DEFAULT);
    }
}