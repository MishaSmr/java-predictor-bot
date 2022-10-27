package com.fchat.bot.predictorbot.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PredictionRepository extends JpaRepository<Prediction, Long> {

    @Query("select p from Prediction p" +
            " where p.user.chatId = ?1" +
            " and p.match.matchId = ?2")
    Prediction findByUserAndMatch(Long chatId, int matchId);

    @Query("select p from Prediction p" +
            " where p.user.chatId = ?1")
    List<Prediction> findByUser(Long chatId);

    @Query("select p from Prediction p" +
            " where p.match.matchId = ?1")
    List<Prediction> findByMatch(int matchId);
}
