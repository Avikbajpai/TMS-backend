package com.nexustms.config;

import com.nexustms.model.*;
import com.nexustms.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import java.time.LocalDateTime;

@Configuration
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {
    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final TicketRepository ticketRepository;

    @Override
    public void run(String... args) {
        if (userRepository.count() > 0) return;

        User customer = User.builder().id("u1").name("Alice Customer").role(UserRole.CUSTOMER).build();
        User engineer = User.builder().id("u2").name("Bob Engineer").role(UserRole.ENGINEER).build();
        User admin = User.builder().id("u3").name("Charlie Admin").role(UserRole.ADMIN).build();

        userRepository.save(customer);
        userRepository.save(engineer);
        userRepository.save(admin);

        ticketRepository.save(Ticket.builder()
                .id("T-1001")
                .title("Network Latency")
                .description("Slow response time in the APAC region.")
                .status(TicketStatus.OPEN)
                .priority(TicketPriority.HIGH)
                .category("Infrastructure")
                .createdBy(customer)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build());
    }
}
