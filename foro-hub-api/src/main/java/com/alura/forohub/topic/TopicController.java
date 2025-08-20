package com.alura.forohub.topic;

import com.alura.forohub.topic.TopicDtos.CreateTopicRequest;
import com.alura.forohub.topic.TopicDtos.UpdateTopicRequest;
import com.alura.forohub.topic.TopicDtos.TopicResponse;
import com.alura.forohub.user.User;
import com.alura.forohub.user.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/topics")
public class TopicController {

    private final TopicRepository topicRepository;
    private final UserRepository userRepository;

    public TopicController(TopicRepository topicRepository, UserRepository userRepository) {
        this.topicRepository = topicRepository;
        this.userRepository = userRepository;
    }

    @PostMapping
    public ResponseEntity<TopicResponse> create(@RequestBody @Valid CreateTopicRequest req, Authentication auth) {
        String username = (String) auth.getPrincipal();
        User author = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));
        if (topicRepository.existsByTitleAndMessage(req.title, req.message)) {
            throw new IllegalArgumentException("Tópico duplicado: ya existe con el mismo título y mensaje");
        }
        Topic t = new Topic(req.title, req.message, req.course, author);
        topicRepository.save(t);
        return ResponseEntity.ok(new TopicResponse(t));
    }

    @GetMapping
    public ResponseEntity<Page<TopicResponse>> list(Pageable pageable) {
        Page<TopicResponse> page = topicRepository.findAll(pageable).map(TopicResponse::new);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TopicResponse> get(@PathVariable Long id) {
        Topic t = topicRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tópico no encontrado"));
        return ResponseEntity.ok(new TopicResponse(t));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TopicResponse> update(@PathVariable Long id, @RequestBody @Valid UpdateTopicRequest req, Authentication auth) {
        Topic t = topicRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tópico no encontrado"));
        String username = (String) auth.getPrincipal();
        if (!t.getAuthor().getUsername().equals(username)) {
            return ResponseEntity.status(403).build();
        }
        t.setTitle(req.title);
        t.setMessage(req.message);
        t.setCourse(req.course);
        t.setStatus(req.status);
        topicRepository.save(t);
        return ResponseEntity.ok(new TopicResponse(t));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id, Authentication auth) {
        Topic t = topicRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tópico no encontrado"));
        String username = (String) auth.getPrincipal();
        if (!t.getAuthor().getUsername().equals(username)) {
            return ResponseEntity.status(403).build();
        }
        topicRepository.delete(t);
        return ResponseEntity.noContent().build();
    }
}
