package com.eventbooking.Event.Booking.Services;

import com.eventbooking.Event.Booking.DTO.EventDTO;
import com.eventbooking.Event.Booking.Entities.EventEntity;
import com.eventbooking.Event.Booking.Entities.SeatEntity;
import com.eventbooking.Event.Booking.Exceptions.ResourceNotFoundException;
import com.eventbooking.Event.Booking.Repositories.EventRepository;
import com.eventbooking.Event.Booking.Repositories.SeatRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EventService {
    private final EventRepository eventRepository;
    private final SeatRepository seatRepository;
    private final ModelMapper modelMapper;

    private static final String UPLOAD_DIR = "uploads";

    public List<EventDTO> getAllEvents() {
        return eventRepository.findAll()
                .stream()
                .map(eventEntity -> modelMapper.map(eventEntity, EventDTO.class))
                .toList();
    }

    public EventDTO getEventById(Long id) {
        EventEntity eventEntity = eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Event Not Found with id: " + id));
        return modelMapper.map(eventEntity, EventDTO.class);
    }

    public EventDTO createEvent(EventDTO eventDTO) {
        EventEntity eventEntity = modelMapper.map(eventDTO, EventEntity.class);
        return modelMapper.map(eventRepository.save(eventEntity), EventDTO.class);
    }

    public EventDTO updateEvent(Long id, EventDTO eventDTO) {
        EventEntity eventEntity = eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Event Not Found with id: " + id));

        eventEntity.setName(eventDTO.getName());
        eventEntity.setLocation(eventDTO.getLocation());
        eventEntity.setLocalDate(eventDTO.getLocalDate());
        eventEntity.setTotalSeats(eventDTO.getTotalSeats());
        eventEntity.setAvailableSeats(eventDTO.getAvailableSeats());
        eventEntity.setPrice(eventDTO.getPrice());

        return modelMapper.map(eventRepository.save(eventEntity), EventDTO.class);
    }

    public EventDTO createEvent(String name, String date, String location,
            Long totalSeats, Double price, MultipartFile image) throws IOException {

        EventEntity entity = new EventEntity();
        entity.setName(name);
        entity.setLocalDate(LocalDate.parse(date));
        entity.setLocation(location);
        entity.setTotalSeats(totalSeats);
        entity.setAvailableSeats(totalSeats);
        entity.setPrice(price);

        if (image != null && !image.isEmpty()) {

            Path uploadPath = Paths.get(UPLOAD_DIR);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            String fileName = UUID.randomUUID() + "_" + image.getOriginalFilename();
            Path filePath = uploadPath.resolve(fileName);

            Files.write(filePath, image.getBytes());

            entity.setImageUrl("/uploads/" + fileName);
        }

        EventEntity saved = eventRepository.save(entity);

        // Initialize seats for this event
        int seatsPerRow = 10;
        long seatNumber = 1;
        int rows = (int) Math.ceil((double) totalSeats / seatsPerRow);
        for (int r = 1; r <= rows; r++) {
            for (int c = 1; c <= seatsPerRow; c++) {
                if (seatNumber > totalSeats)
                    break;
                String seatId = "R" + r + "C" + c;
                SeatEntity seat = SeatEntity.builder()
                        .seatNumber(seatId)
                        .booked(false)
                        .event(saved)
                        .build();
                seatRepository.save(seat);
                seatNumber++;
            }
        }

        return modelMapper.map(saved, EventDTO.class);
    }

    public void deleteEvent(Long id) {
        EventEntity eventEntity = eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Event Not Found with id: " + id));
        eventRepository.delete(eventEntity);
    }

    public EventEntity getEventEntityById(Long id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Event Not Found with id: " + id));
    }

    public void saveEventEntity(EventEntity event) {
        eventRepository.save(event);
    }
}
