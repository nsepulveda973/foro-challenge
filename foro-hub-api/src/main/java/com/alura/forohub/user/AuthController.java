package com.alura.forohub.user;

import com.alura.forohub.security.jwt.JwtService;
import com.alura.forohub.user.AuthDtos.LoginRequest;
import com.alura.forohub.user.AuthDtos.RegisterRequest;
import com.alura.forohub.user.AuthDtos.AuthResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid RegisterRequest req) {
        if (userRepository.existsByUsername(req.username)) {
            return ResponseEntity.badRequest().body(Map.of("message", "Usuario ya existe"));
        }
        User user = new User(req.username, passwordEncoder.encode(req.password), Set.of("ROLE_USER"));
        userRepository.save(user);
        String token = jwtService.generateToken(user.getUsername(), Map.of("roles", user.getRoles()));
        return ResponseEntity.ok(new AuthResponse(token));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginRequest req) {
        var userOpt = userRepository.findByUsername(req.username);
        if (userOpt.isEmpty()) return ResponseEntity.status(401).body(Map.of("message", "Credenciales inválidas"));
        var user = userOpt.get();
        // Manually verify password (we are not using AuthenticationManager for brevity)
        // In a larger app, delegate to AuthenticationManager
        if (!passwordEncoder.matches(req.password, user.getPassword())) {
            return ResponseEntity.status(401).body(Map.of("message", "Credenciales inválidas"));
        }
        String token = jwtService.generateToken(user.getUsername(), Map.of("roles", user.getRoles()));
        return ResponseEntity.ok(new AuthResponse(token));
    }
}
