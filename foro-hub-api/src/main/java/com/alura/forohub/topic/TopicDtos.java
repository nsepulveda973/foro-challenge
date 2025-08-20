package com.alura.forohub.topic;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;

public class TopicDtos {

    public static class CreateTopicRequest {
        @NotBlank @Size(min=5, max=200) public String title;
        @NotBlank @Size(min=10, max=4000) public String message;
        @NotBlank @Size(min=2, max=100) public String course;
    }

    public static class UpdateTopicRequest {
        @NotNull public Long id;
        @NotBlank @Size(min=5, max=200) public String title;
        @NotBlank @Size(min=10, max=4000) public String message;
        @NotBlank @Size(min=2, max=100) public String course;
        @NotNull public TopicStatus status;
    }

    public static class TopicResponse {
        public Long id;
        public String title;
        public String message;
        public String course;
        public TopicStatus status;
        public String author;
        public Instant createdAt;

        public TopicResponse(Topic t) {
            this.id = t.getId();
            this.title = t.getTitle();
            this.message = t.getMessage();
            this.course = t.getCourse();
            this.status = t.getStatus();
            this.author = t.getAuthor().getUsername();
            this.createdAt = t.getCreatedAt();
        }
    }
}
