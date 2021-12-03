package com.example.reddit.models;

import java.time.Instant;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SubReddit {

    @Id
    @GeneratedValue
    private Long subRedditId;

    @NotBlank
    private String name;

    @NotBlank
    private String description;

    @OneToMany
    private List<Post> posts;

    private Instant createdDate;

    @ManyToOne
    private User user;
}
