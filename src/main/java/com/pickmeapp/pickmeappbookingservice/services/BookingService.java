package com.pickmeapp.pickmeappbookingservice.services;

import com.pickme.pickmeappentityservice.models.Booking;
import com.pickmeapp.pickmeappbookingservice.dtos.CreateBookingRequestDto;
import com.pickmeapp.pickmeappbookingservice.dtos.CreateBookingResponseDto;

public interface BookingService {
    public CreateBookingResponseDto createBooking(CreateBookingRequestDto createBookingRequestDto);

    }
