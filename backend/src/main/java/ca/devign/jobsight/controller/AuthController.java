package ca.devign.jobsight.controller;

import ca.devign.jobsight.dto.*;
import ca.devign.jobsight.model.User;
import ca.devign.jobsight.repository.UserRepository;
import ca.devign.jobsight.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepo;
    private final JwtUtil jwtUtil;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @PostMapping("/register")
    public AuthResponse register(@RequestBody RegisterRequest request) {
        User user = new User();
        user.setEmail(request.email);
        user.setPassword(encoder.encode(request.password));
        user.setFullName(request.fullName);
        userRepo.save(user);
        return new AuthResponse(jwtUtil.generateToken(user.getEmail()));
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody AuthRequest request) {
        User user = userRepo.findByEmail(request.email).orElseThrow();
        if (!encoder.matches(request.password, user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }
        return new AuthResponse(jwtUtil.generateToken(user.getEmail()));
    }
}