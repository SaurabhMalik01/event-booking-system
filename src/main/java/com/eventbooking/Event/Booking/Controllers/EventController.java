package com.eventbooking.Event.Booking.Controllers;

import com.eventbooking.Event.Booking.DTO.EventDTO;
import com.eventbooking.Event.Booking.Services.EventService;
import com.eventbooking.Event.Booking.Repositories.SeatRepository;
import com.eventbooking.Event.Booking.Entities.SeatEntity;
import com.eventbooking.Event.Booking.Entities.EventEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;
    private final SeatRepository seatRepository;

    @GetMapping
    public List<EventDTO> getAllEvents() {
        return eventService.getAllEvents();
    }

    @GetMapping("/{id}")
    public EventDTO getEventById(@PathVariable Long id) {
        return eventService.getEventById(id);
    }

    @PostMapping("/create")
    public EventDTO createEvent(
            @RequestParam String name,
            @RequestParam String date,
            @RequestParam String location,
            @RequestParam Long totalSeats,
            @RequestParam Double price,
            @RequestParam(required = false) MultipartFile image) throws IOException {
        return eventService.createEvent(name, date, location, totalSeats, price, image);
    }

    @PutMapping("/{id}")
    public EventDTO updateEvent(@PathVariable Long id, @RequestBody EventDTO eventDTO) {
        return eventService.updateEvent(id, eventDTO);
    }

    @DeleteMapping("/{id}")
    public void deleteEvent(@PathVariable Long id) {
        eventService.deleteEvent(id);
    }

    @GetMapping("/{eventId}/seats")
    public Map<String, Object> getSeatMap(@PathVariable Long eventId) {
        EventDTO eventDTO = eventService.getEventById(eventId);
        EventEntity event = eventService.getEventEntityById(eventId); // Add this method to EventService
        long totalSeats = eventDTO.getTotalSeats();
        int seatsPerRow = 10;
        int rows = (int) Math.ceil((double) totalSeats / seatsPerRow);
        int cols = seatsPerRow;

        Map<String, Boolean> seats = new HashMap<>();
        List<SeatEntity> seatEntities = seatRepository.findByEvent(event);
        for (SeatEntity seat : seatEntities) {
            seats.put(seat.getSeatNumber(), seat.isBooked());
        }

        Map<String, Object> result = new HashMap<>();
        result.put("rows", rows);
        result.put("cols", cols);
        result.put("seats", seats);
        return result;
    }

    @PostMapping("/{eventId}/book-seats")
    public Map<String, Object> bookSeats(@PathVariable Long eventId, @RequestBody List<String> seatNumbers) {
        EventEntity event = eventService.getEventEntityById(eventId);
        List<SeatEntity> seats = seatRepository.findByEvent(event);
        int bookedCount = 0;
        for (String seatNum : seatNumbers) {
            SeatEntity seat = seatRepository.findByEventAndSeatNumber(event, seatNum);
            if (seat != null && !seat.isBooked()) {
                seat.setBooked(true);
                seatRepository.save(seat);
                bookedCount++;
            }
        }
        // Optionally update availableSeats in EventEntity
        event.setAvailableSeats(event.getAvailableSeats() - bookedCount);
        eventService.saveEventEntity(event);

        Map<String, Object> resp = new HashMap<>();
        resp.put("success", true);
        resp.put("booked", bookedCount);
        return resp;
    }
}
