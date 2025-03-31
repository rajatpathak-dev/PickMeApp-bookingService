package com.pickmeapp.pickmeappbookingservice.dtos;


import com.pickme.pickmeappentityservice.models.ExactLocation;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RideRequestDto {
    private Long passengerId;
    private ExactLocation startLocation;
    private ExactLocation endLocation;
    private List<Long> driverId;
    private Long bookingId;
}
