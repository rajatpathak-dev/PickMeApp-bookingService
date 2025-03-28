package com.pickmeapp.pickmeappbookingservice.dtos;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NearbyDriversRequestDto {
    Double latitude;
    Double longitude;
}
