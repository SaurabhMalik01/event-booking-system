package com.eventbooking.Event.Booking.Repositories;

import com.eventbooking.Event.Booking.Entities.BookingEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookingRepository extends JpaRepository<BookingEntity,Long> {
    List<BookingEntity> findByUserId(Long userId);
}
