INSERT INTO users (id, username, password) VALUES
(1, 'demo', '$2a$10$Z1v8rPfj3v6nIvN4oYq2ve1v8Xz7tQ8oQqEwT3bqH6U2V0oK3PqzC'); -- password: demo

INSERT INTO user_roles (user_id, role) VALUES
(1, 'ROLE_USER');
