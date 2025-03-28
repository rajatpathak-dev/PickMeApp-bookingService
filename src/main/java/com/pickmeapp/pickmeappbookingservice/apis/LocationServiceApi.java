package com.pickmeapp.pickmeappbookingservice.apis;

import com.pickmeapp.pickmeappbookingservice.dtos.DriverLocationDto;
import com.pickmeapp.pickmeappbookingservice.dtos.NearbyDriversRequestDto;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface LocationServiceApi {

    @POST("/api/location/nearby/drivers")
    Call<DriverLocationDto[]> getNearsetDrivers(@Body NearbyDriversRequestDto requestDto);
}
