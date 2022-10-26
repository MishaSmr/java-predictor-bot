package com.fchat.bot.predictorbot.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MatchDto {
    private int matchId;
    private int team1Id;
    private int team2Id;
    private int scores1;
    private int scores2;
    private String startTime;
}
