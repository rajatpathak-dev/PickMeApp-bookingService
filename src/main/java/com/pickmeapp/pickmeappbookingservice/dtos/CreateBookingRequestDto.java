package com.pickmeapp.pickmeappbookingservice.dtos;

import com.pickme.pickmeappentityservice.models.ExactLocation;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateBookingRequestDto {
    private Long passengerId;

    private ExactLocation startLocation;

    private ExactLocation endLocation;
}
