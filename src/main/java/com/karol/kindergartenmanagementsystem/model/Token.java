package com.karol.kindergartenmanagementsystem.model;

import jakarta.persistence.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "token")
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
