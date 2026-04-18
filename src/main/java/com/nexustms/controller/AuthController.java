package com.nexustms.controller;

import com.nexustms.auth.JwtUtil;
import com.nexustms.dto.UserResponse;
import com.nexustms.model.User;
import com.nexustms.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AuthController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/auth/login")
    public ResponseEntity<?> login(@RequestBody User body) {
        ResponseEntity<?> response = null;
        try{
            String username = body.getUsername();
            String password = body.getPassword();
            User user = userRepository.findByUsername(username)
                    .orElseThrow();
            if (!passwordEncoder.matches(password, user.getPassword())) {
                return ResponseEntity.status(401).body(Map.of("success", false));
            }
            UserResponse userResponse = UserResponse.builder()
                    .username(user.getUsername())
                    .id(user.getId())
                    .role(user.getRole().name())
                    .name(user.getName())
                    .build();

            String token = jwtUtil.generateToken(user);
            response = ResponseEntity.ok(Map.of(
                    "success", true,
                    "token", token,
                    "data", userResponse
            ));
        }catch(Exception ex){
            System.err.print(ex.getMessage());
        }
        return response;
    }
}
