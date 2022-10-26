package com.fchat.bot.predictorbot.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MatchScoreDto {
    private int matchId;
    private int scores1;
    private int scores2;
}
