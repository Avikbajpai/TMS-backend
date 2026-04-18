package com.nexustms.service;

import com.nexustms.dto.*;
import com.nexustms.model.*;
import com.nexustms.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TicketService {
    @Autowired
    private final TicketRepository ticketRepository;
    @Autowired
    private final UserRepository userRepository;

    public List<TicketResponse> getAllTickets() {
        return ticketRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::mapUserResponse)
                .collect(Collectors.toList());
    }

    public UserResponse createUser(User user){
        User addedUser = userRepository.save(user);
        return UserResponse.builder().name(addedUser.getName())
                .id(addedUser.getId())
                .role(addedUser.getRole().name())
                .build();
    }

    public TicketResponse createTicket(CreateTicketRequest request, String userId) {
        User creator = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        Ticket ticket = Ticket.builder()
                .id(UUID.randomUUID().toString().substring(0, 8))
                .title(request.getTitle())
                .description(request.getDescription())
                .priority(request.getPriority())
                .category(request.getCategory())
                .status(TicketStatus.OPEN)
                .createdBy(creator)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        return mapToResponse(ticketRepository.save(ticket));
    }

    public TicketResponse updateStatus(String ticketId, TicketStatus newStatus, User actor) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Ticket not found"));

        // Engineer Pick Up Logic
        if (actor.getRole() == UserRole.ENGINEER && newStatus == TicketStatus.IN_PROGRESS) {
            if (ticket.getAssignedTo() == null) {
                ticket.setAssignedTo(actor);
            } else if (!ticket.getAssignedTo().getId().equals(actor.getId())) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Ticket already assigned to another engineer");
            }
        }

        // Customer Logic
        if (actor.getRole() == UserRole.CUSTOMER && !ticket.getCreatedBy().getId().equals(actor.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied");
        }

        ticket.setStatus(newStatus);
        ticket.setUpdatedAt(LocalDateTime.now());
        return mapToResponse(ticketRepository.save(ticket));
    }

    public TicketResponse assignTicket(String ticketId, String engineerId) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Ticket not found"));
        User engineer = userRepository.findById(engineerId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Engineer not found"));

        if (engineer.getRole() != UserRole.ENGINEER) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User is not an engineer");
        }

        ticket.setAssignedTo(engineer);
        ticket.setUpdatedAt(LocalDateTime.now());
        return mapToResponse(ticketRepository.save(ticket));
    }

    public DashboardStatsResponse getStats() {
        List<Ticket> all = ticketRepository.findAll();
        Map<String, Long> byPriority = new HashMap<>();
        for (TicketPriority p : TicketPriority.values()) {
            byPriority.put(p.name(), all.stream().filter(t -> t.getPriority() == p).count());
        }

        return DashboardStatsResponse.builder()
                .total(all.size())
                .open(all.stream().filter(t -> t.getStatus() == TicketStatus.OPEN).count())
                .inProgress(all.stream().filter(t -> t.getStatus() == TicketStatus.IN_PROGRESS).count())
                .resolved(all.stream().filter(t -> t.getStatus() == TicketStatus.RESOLVED).count())
                .byPriority(byPriority)
                .build();
    }

    public List<User> getEngineers() {
        return userRepository.findByRole(UserRole.ENGINEER);
    }

    private TicketResponse mapToResponse(Ticket t) {
        return TicketResponse.builder()
                .id(t.getId())
                .title(t.getTitle())
                .description(t.getDescription())
                .status(t.getStatus())
                .priority(t.getPriority())
                .category(t.getCategory())
                .createdAt(t.getCreatedAt())
                .updatedAt(t.getUpdatedAt())
                .createdBy(UserResponse.builder()
                        .id(t.getCreatedBy().getId())
                        .name(t.getCreatedBy().getName())
                        .role(t.getCreatedBy().getRole().name())
                        .build())
                .assignedTo(t.getAssignedTo() == null ? null : UserResponse.builder()
                        .id(t.getAssignedTo().getId())
                        .name(t.getAssignedTo().getName())
                        .role(t.getAssignedTo().getRole().name())
                        .build())
                .build();
    }

    private UserResponse mapUserResponse(User user){
        return UserResponse.builder()
                .role(user.getRole().name())
                .id(user.getId())
                .name(user.getName())
                .build();
    }
}
