package com.eventbooking.Event.Booking.Entities;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.cglib.core.Local;

import java.time.LocalDate;


@Entity
@Table(name = "EventData")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class EventEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private LocalDate localDate;
    private String location;
    private Long totalSeats;
    private Long availableSeats;
    private Double price;

    private String imageUrl;
}
