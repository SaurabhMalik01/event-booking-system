package com.eventbooking.Event.Booking.Entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "UserData")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Column(unique = true, nullable = false)
    private String email;
    private String password;
}
