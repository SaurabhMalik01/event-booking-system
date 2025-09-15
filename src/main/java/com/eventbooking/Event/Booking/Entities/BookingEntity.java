package com.eventbooking.Event.Booking.Entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "BookingData")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class BookingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookingId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "event_id")
    private EventEntity event;

    private Long seatsBooked;
    private Double totalAmount;
    private String paymentMethod;
    private Boolean paymentStatus;
}
