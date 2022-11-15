package com.fchat.bot.predictorbot.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query("select u from User u" +
            " left outer join GroupUser g on u.chatId = g.user.chatId" +
            " where g.groupId = ?1")
    List<User> findByGroup(Long groupId);
}
