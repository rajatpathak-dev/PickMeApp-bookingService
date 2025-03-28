package com.pickmeapp.pickmeappbookingservice.controller;

import com.pickmeapp.pickmeappbookingservice.dtos.CreateBookingRequestDto;
import com.pickmeapp.pickmeappbookingservice.dtos.CreateBookingResponseDto;
import com.pickmeapp.pickmeappbookingservice.services.BookingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/booking")
public class BookingController {
    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping("")
    public ResponseEntity<CreateBookingResponseDto> createBooking(@RequestBody CreateBookingRequestDto createBookingRequestDto){
        return new ResponseEntity<>(bookingService.createBooking(createBookingRequestDto), HttpStatus.CREATED);
    }

}
