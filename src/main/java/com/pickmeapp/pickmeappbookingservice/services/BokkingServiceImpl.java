package com.pickmeapp.pickmeappbookingservice.services;

import com.pickme.pickmeappentityservice.models.Booking;
import com.pickme.pickmeappentityservice.models.BookingStatus;
import com.pickme.pickmeappentityservice.models.Passenger;
import com.pickmeapp.pickmeappbookingservice.apis.LocationServiceApi;
import com.pickmeapp.pickmeappbookingservice.dtos.CreateBookingRequestDto;
import com.pickmeapp.pickmeappbookingservice.dtos.CreateBookingResponseDto;
import com.pickmeapp.pickmeappbookingservice.dtos.DriverLocationDto;
import com.pickmeapp.pickmeappbookingservice.dtos.NearbyDriversRequestDto;
import com.pickmeapp.pickmeappbookingservice.repositories.BookingRepository;
import com.pickmeapp.pickmeappbookingservice.repositories.PassengerRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class BokkingServiceImpl implements BookingService{

    private final PassengerRepository passengerRepository;
    private final BookingRepository bookingRepository;
    private final RestTemplate restTemplate;
    private final LocationServiceApi locationServiceApi;
//    private static final String LOCATION_SERVICE = "http://localhost:7777";

    public BokkingServiceImpl(PassengerRepository passengerRepository,
                              BookingRepository bookingRepository,
                               LocationServiceApi locationServiceApi ) {
        this.passengerRepository = passengerRepository;
        this.bookingRepository = bookingRepository;
        this.restTemplate = new RestTemplate();
        this.locationServiceApi = locationServiceApi;
    }

    @Override
    public CreateBookingResponseDto createBooking(CreateBookingRequestDto createBookingRequestDto) {
        Optional<Passenger> passengerOptional = passengerRepository.findById(createBookingRequestDto.getPassengerId());
        Booking booking = Booking.builder()
                                 .bookingStatus(BookingStatus.ASSIGNING_DRIVER)
                                 .startLocation(createBookingRequestDto.getStartLocation())
                                 .endLocation(createBookingRequestDto.getEndLocation())
                                  .passenger(passengerOptional.get())
                                 .build();

        NearbyDriversRequestDto request = NearbyDriversRequestDto.builder()
                .latitude(createBookingRequestDto.getStartLocation().getLatitude())
                .longitude(createBookingRequestDto.getStartLocation().getLongitude())
                .build();
        processNearByDriversAsync(request);
//
//        ResponseEntity<DriverLocationDto[]> result = restTemplate.postForEntity(LOCATION_SERVICE+"/api/location/nearby/drivers",request, DriverLocationDto[].class);
//
//        if(result.getStatusCode().is2xxSuccessful() && result.getBody() != null) {
//            List<DriverLocationDto> driverLocations = Arrays.asList(result.getBody());
//            driverLocations.forEach(driverLocationDto -> {
//                System.out.println(driverLocationDto.getDriverId() + " " + "lat: " + driverLocationDto.getLatitude() + "long: " + driverLocationDto.getLongitude());
//            });
//        }
         Booking newBooking = bookingRepository.save(booking);
        return CreateBookingResponseDto.builder()
                .bookingId(newBooking.getId())
                .bookingStatus(newBooking.getBookingStatus().toString())
                .build();
    }

    public void processNearByDriversAsync(NearbyDriversRequestDto nearbyDriversRequestDto){
        Call<DriverLocationDto[]> call = locationServiceApi.getNearsetDrivers(nearbyDriversRequestDto);
        call.enqueue(new Callback<DriverLocationDto[]>() {
            @Override
            public void onResponse(Call<DriverLocationDto[]> call, Response<DriverLocationDto[]> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<DriverLocationDto> driverLocations = Arrays.asList(response.body());
                    driverLocations.forEach(driverLocationDto -> {
                        System.out.println(driverLocationDto.getDriverId() + " " + "lat: " + driverLocationDto.getLatitude() + "long: " + driverLocationDto.getLongitude());
                    });
                }else{
                    System.out.println("Response failed "+response.message());
                }
            }

            @Override
            public void onFailure(Call<DriverLocationDto[]> call, Throwable throwable) {
                throwable.printStackTrace();
            }
        });
    }
}
