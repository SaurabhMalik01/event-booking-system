package com.eventbooking.Event.Booking.Controllers;

import com.eventbooking.Event.Booking.DTO.BookingDTO;
import com.eventbooking.Event.Booking.Services.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bookings")
@RequiredArgsConstructor
public class BookingController {

    public final BookingService bookingService;

    @PostMapping()
    public BookingDTO createBooking(@RequestBody BookingDTO data){
        return bookingService.createBooking(data);
    }

    @GetMapping("/{id}")
    public BookingDTO getBookingById(@PathVariable Long id){
        return bookingService.getBookingById(id);
    }

    @GetMapping("/user/{userId}")
    public List<BookingDTO> getBookingByUser(@PathVariable Long userId){
        return bookingService.getBookingsByUser(userId);
    }

}
