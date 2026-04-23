package com.nexustms.controller;

import com.nexustms.dto.*;
import com.nexustms.model.TicketStatus;
import com.nexustms.model.User;
import com.nexustms.model.UserRole;
import com.nexustms.repository.UserRepository;
import com.nexustms.service.TicketService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class TicketController {
    @Autowired
    private final TicketService ticketService;
    @Autowired
    private final UserRepository userRepository;
    @Autowired
    PasswordEncoder passwordEncoder;

    @GetMapping("/tickets")
    public ApiResponse<List<TicketResponse>> getTickets() {
        return ApiResponse.<List<TicketResponse>>builder()
                .success(true)
                .data(ticketService.getAllTickets())
                .build();
    }

    @GetMapping("/users")
    public ApiResponse<List<UserResponse>> getUsers() {
        return ApiResponse.<List<UserResponse>>builder()
                .success(true)
                .data(ticketService.getAllUsers())
                .build();
    }

    @PostMapping("/tickets")
    public ApiResponse<TicketResponse> createTicket(
            @Valid @RequestBody CreateTicketRequest request,
            HttpServletRequest httpRequest) {
        try {
            Integer userId = (Integer) httpRequest.getAttribute("userId");

            return ApiResponse.<TicketResponse>builder()
                    .success(true)
                    .data(ticketService.createTicket(request, Long.valueOf(userId)))
                    .build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/user")
    public ApiResponse<UserResponse> createUser(@Valid @RequestBody User request) {

        String password = request.getPassword();
        request.setPassword(passwordEncoder.encode(password));
        return ApiResponse.<UserResponse>builder()
                .success(true)
                .data(ticketService.createUser(request))
                .build();
    }

    @PutMapping("/tickets/{id}/status")
    public ApiResponse<TicketResponse> updateStatus(
            @PathVariable String id,
            @RequestBody UpdateStatusRequest request,
            HttpServletRequest httpRequest) {
        Integer userId = (Integer) httpRequest.getAttribute("userId");
        User actor = userRepository.findById(Long.valueOf(userId))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return ApiResponse.<TicketResponse>builder()
                .success(true)
                .data(ticketService.updateStatus(id, request.getStatus(), actor))
                .build();
    }

    @PutMapping("/tickets/{id}/assign")
    public ApiResponse<TicketResponse> assignTicket(
            @PathVariable String id,
            @RequestBody Map<String, String> body) {
        return ApiResponse.<TicketResponse>builder()
                .success(true)
                .data(ticketService.assignTicket(id, body.get("engineerId")))
                .build();
    }

    @GetMapping("/dashboard/stats")
    public ApiResponse<DashboardStatsResponse> getStats() {
        return ApiResponse.<DashboardStatsResponse>builder()
                .success(true)
                .data(ticketService.getStats())
                .build();
    }

    @GetMapping("/engineers")
    public ApiResponse<List<User>> getEngineers() {
        return ApiResponse.<List<User>>builder()
                .success(true)
                .data(ticketService.getEngineers())
                .build();
    }
}
