package com.alura.forohub.topic;

import com.alura.forohub.user.User;
import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "topics")
public class Topic {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false)
    private String title;

    @Column(nullable=false, length = 4000)
    private String message;

    @Column(nullable=false)
    private String course;

    @Enumerated(EnumType.STRING)
    private TopicStatus status = TopicStatus.OPEN;

    @ManyToOne(optional = false)
    private User author;

    @Column(nullable=false, updatable = false)
    private Instant createdAt = Instant.now();

    public Topic(){}

    public Topic(String title, String message, String course, User author) {
        this.title = title;
        this.message = message;
        this.course = course;
        this.author = author;
        this.status = TopicStatus.OPEN;
    }

    public Long getId() { return id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public String getCourse() { return course; }
    public void setCourse(String course) { this.course = course; }
    public TopicStatus getStatus() { return status; }
    public void setStatus(TopicStatus status) { this.status = status; }
    public User getAuthor() { return author; }
    public void setAuthor(User author) { this.author = author; }
    public Instant getCreatedAt() { return createdAt; }
}
