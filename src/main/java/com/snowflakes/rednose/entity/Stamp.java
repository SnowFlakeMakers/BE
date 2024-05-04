package com.snowflakes.rednose.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;

@Entity
@Getter
public class Stamp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "image_url", nullable = false)
    private String imageUrl;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "number_of_likes", nullable = false)
    private int numberOfLikes;

    public Stamp() {
    }

    @Builder
    public Stamp(Long id, String name, String imageUrl, LocalDateTime createdAt, int numberOfLikes) {
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
        this.createdAt = createdAt;
        this.numberOfLikes = numberOfLikes;
    }

    @Override
    public String toString() {
        return "Stamp{" +
                "id=" + id +
                ", createdAt=" + createdAt +
                ", numberOfLikes=" + numberOfLikes +
                '}';
    }
}