package com.example.reddit.model;

import static javax.persistence.FetchType.LAZY;

import java.time.Instant;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "token")
public class VerificationToken {

    @Id
    @GeneratedValue
    private Long tokenId;

    private String token;

    @OneToOne(fetch = LAZY)
    private RedditUser user;

    private Instant expiryDate;
}