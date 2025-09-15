package com.eventbooking.Event.Booking.DTO;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.time.LocalDate;


@Data
public class EventDTO {
    private Long id;

    @NotBlank(message = "Event name is required")
    private String name;

    @NotNull(message = "Event date is required")
    private LocalDate localDate;

    @NotBlank(message = "Location is required")
    private String location;

    @NotNull(message = "Total seats are required")
    @Min(value = 1, message = "Total seats must be at least 1")
    private Long totalSeats;

    @NotNull(message = "Available seats are required")
    @Min(value = 0, message = "Available seats cannot be negative")
    private Long availableSeats;

    @NotNull(message = "Price is required")
    @Positive(message = "Price must be greater than 0")
    private Double price;

    private String imageUrl;
}
