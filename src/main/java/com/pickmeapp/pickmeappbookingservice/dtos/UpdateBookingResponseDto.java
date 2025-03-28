package com.pickmeapp.pickmeappbookingservice.dtos;

import com.pickme.pickmeappentityservice.models.BookingStatus;
import com.pickme.pickmeappentityservice.models.Driver;
import lombok.*;

import java.util.Optional;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateBookingResponseDto {

    private Long bookingId;
    private BookingStatus status;
    private Optional<Driver> driver;
}
