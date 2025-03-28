package com.pickmeapp.pickmeappbookingservice.dtos;

import com.pickme.pickmeappentityservice.models.BookingStatus;
import lombok.*;

import java.util.Optional;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateBookingRequestDto {
    private String status;
    private Optional<Long> driverId;
}
