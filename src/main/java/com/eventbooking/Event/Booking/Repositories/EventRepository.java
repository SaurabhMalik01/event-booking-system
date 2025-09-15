package com.eventbooking.Event.Booking.Repositories;

import com.eventbooking.Event.Booking.Entities.EventEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<EventEntity,Long> {
}
