package com.pickmeapp.pickmeappbookingservice.apis;

import com.pickmeapp.pickmeappbookingservice.dtos.RideRequestDto;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface PickMeSocketApi {

    @POST("/api/socket/newride")
    Call<Boolean> raiseRideRequest(@Body RideRequestDto rideRequestDto);
}
