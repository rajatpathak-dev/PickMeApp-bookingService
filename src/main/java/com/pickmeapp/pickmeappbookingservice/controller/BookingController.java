package com.pickmeapp.pickmeappbookingservice.controller;

import com.pickmeapp.pickmeappbookingservice.dtos.CreateBookingRequestDto;
import com.pickmeapp.pickmeappbookingservice.dtos.CreateBookingResponseDto;
import com.pickmeapp.pickmeappbookingservice.dtos.UpdateBookingRequestDto;
import com.pickmeapp.pickmeappbookingservice.dtos.UpdateBookingResponseDto;
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

    @PatchMapping("/{bookingId}")
    public ResponseEntity<UpdateBookingResponseDto> updateBooking(@RequestBody UpdateBookingRequestDto updateBookingRequestDto,@PathVariable Long bookingId){
        return new ResponseEntity<>(bookingService.updateBooking(updateBookingRequestDto, bookingId), HttpStatus.OK);
    }

}
