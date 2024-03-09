package com.karol.kindergartenmanagementsystem.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "token")
@Getter
@Setter
public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String token;
    private boolean loggedOut;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
