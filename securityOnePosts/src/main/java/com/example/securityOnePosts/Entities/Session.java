package com.example.securityOnePosts.Entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Session {
     @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
     private String refreshToken;
     @CreationTimestamp
     private LocalDateTime lastUsedAt;
    @ManyToOne
    private User user;
}
