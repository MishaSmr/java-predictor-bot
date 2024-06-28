package com.fchat.bot.predictorbot.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface MatchRepository extends JpaRepository<Match, Integer> {

    @Query("select m from Match m" +
            " where m.apiId = ?1")
    Match findByApiId(Integer apiId);
}
