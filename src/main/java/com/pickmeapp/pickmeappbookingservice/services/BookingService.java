package com.pickmeapp.pickmeappbookingservice.services;

import com.pickme.pickmeappentityservice.models.Booking;
import com.pickmeapp.pickmeappbookingservice.dtos.CreateBookingRequestDto;
import com.pickmeapp.pickmeappbookingservice.dtos.CreateBookingResponseDto;
import com.pickmeapp.pickmeappbookingservice.dtos.UpdateBookingRequestDto;
import com.pickmeapp.pickmeappbookingservice.dtos.UpdateBookingResponseDto;

public interface BookingService {
    public CreateBookingResponseDto createBooking(CreateBookingRequestDto createBookingRequestDto);
    public UpdateBookingResponseDto updateBooking(UpdateBookingRequestDto updateBookingRequestDto,Long bookingId);

    }
