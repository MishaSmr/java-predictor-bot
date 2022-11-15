package com.fchat.bot.predictorbot.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "group_user")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroupUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "group_id")
    private Long groupId;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
