package com.pickmeapp.pickmeappbookingservice.dtos;

import com.pickme.pickmeappentityservice.models.BookingStatus;
import lombok.*;

import java.util.Optional;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class UpdateBookingRequestDto {
    private String status;
    private Long bookingId;
    private Optional<Long> driverId;
}
