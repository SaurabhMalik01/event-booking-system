package com.eventbooking.Event.Booking.Repositories;

import com.eventbooking.Event.Booking.Entities.SeatEntity;
import com.eventbooking.Event.Booking.Entities.EventEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SeatRepository extends JpaRepository<SeatEntity, Long> {
    List<SeatEntity> findByEvent(EventEntity event);

    SeatEntity findByEventAndSeatNumber(EventEntity event, String seatNumber);
}