package com.eventbooking.Event.Booking.Services;

import com.eventbooking.Event.Booking.DTO.BookingDTO;
import com.eventbooking.Event.Booking.Entities.BookingEntity;
import com.eventbooking.Event.Booking.Entities.EventEntity;
import com.eventbooking.Event.Booking.Entities.UserEntity;
import com.eventbooking.Event.Booking.Exceptions.ResourceNotFoundException;
import com.eventbooking.Event.Booking.Exceptions.InsufficientSeatsException;
import com.eventbooking.Event.Booking.Repositories.BookingRepository;
import com.eventbooking.Event.Booking.Repositories.EventRepository;
import com.eventbooking.Event.Booking.Repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final ModelMapper modelMapper;

    public BookingDTO createBooking(BookingDTO bookingData){
        BookingEntity bookingEntity = new BookingEntity();


        UserEntity user = userRepository.findById(bookingData.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + bookingData.getUserId()));


        EventEntity event = eventRepository.findById(bookingData.getEventId())
                .orElseThrow(() -> new ResourceNotFoundException("Event not found with id: " + bookingData.getEventId()));


        if (bookingData.getSeatsBooked() > event.getAvailableSeats()) {
            throw new InsufficientSeatsException("Not enough seats available. Only " + event.getAvailableSeats() + " left.");
        }


        event.setAvailableSeats(event.getAvailableSeats() - bookingData.getSeatsBooked());
        eventRepository.save(event);


        bookingEntity.setUser(user);
        bookingEntity.setEvent(event);
        bookingEntity.setSeatsBooked(bookingData.getSeatsBooked());
        bookingEntity.setTotalAmount(bookingData.getTotalAmount());
        bookingEntity.setPaymentMethod(bookingData.getPaymentMethod());
        bookingEntity.setPaymentStatus(bookingData.getPaymentStatus());


        BookingEntity savedBooking = bookingRepository.save(bookingEntity);


        BookingDTO response = new BookingDTO();
        response.setBookingId(savedBooking.getBookingId());
        response.setUserId(savedBooking.getUser().getId());
        response.setEventId(savedBooking.getEvent().getId());
        response.setSeatsBooked(savedBooking.getSeatsBooked());
        response.setTotalAmount(savedBooking.getTotalAmount());
        response.setPaymentMethod(savedBooking.getPaymentMethod());
        response.setPaymentStatus(savedBooking.getPaymentStatus());

        return response;
    }

    public BookingDTO getBookingById(Long id){
        BookingEntity booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No Booking Found with Id: " + id));

        BookingDTO dto = new BookingDTO();
        dto.setBookingId(booking.getBookingId());
        dto.setUserId(booking.getUser().getId());
        dto.setEventId(booking.getEvent().getId());
        dto.setSeatsBooked(booking.getSeatsBooked());
        dto.setTotalAmount(booking.getTotalAmount());
        dto.setPaymentMethod(booking.getPaymentMethod());
        dto.setPaymentStatus(booking.getPaymentStatus());

        return dto;
    }

    public List<BookingDTO> getBookingsByUser(Long userId){
        List<BookingEntity> bookings = bookingRepository.findByUserId(userId);
        if (bookings.isEmpty()) {
            throw new ResourceNotFoundException("No Bookings Found for User Id: " + userId);
        }

        return bookings.stream().map(b -> {
            BookingDTO dto = new BookingDTO();
            dto.setBookingId(b.getBookingId());
            dto.setUserId(b.getUser().getId());
            dto.setEventId(b.getEvent().getId());
            dto.setSeatsBooked(b.getSeatsBooked());
            dto.setTotalAmount(b.getTotalAmount());
            dto.setPaymentMethod(b.getPaymentMethod());
            dto.setPaymentStatus(b.getPaymentStatus());
            return dto;
        }).toList();
    }
}
