package com.pickmeapp.pickmeappbookingservice.services;

import com.pickme.pickmeappentityservice.models.Booking;
import com.pickme.pickmeappentityservice.models.BookingStatus;
import com.pickme.pickmeappentityservice.models.Driver;
import com.pickme.pickmeappentityservice.models.Passenger;
import com.pickmeapp.pickmeappbookingservice.apis.LocationServiceApi;
import com.pickmeapp.pickmeappbookingservice.apis.PickMeSocketApi;
import com.pickmeapp.pickmeappbookingservice.dtos.*;
import com.pickmeapp.pickmeappbookingservice.repositories.BookingRepository;
import com.pickmeapp.pickmeappbookingservice.repositories.DriverRepository;
import com.pickmeapp.pickmeappbookingservice.repositories.PassengerRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class BookingServiceImpl implements BookingService{

    private final PassengerRepository passengerRepository;
    private final BookingRepository bookingRepository;
    private final RestTemplate restTemplate;
    private final LocationServiceApi locationServiceApi;
    private final PickMeSocketApi pickMeSocketApi;
    private final DriverRepository driverRepository;
//    private static final String LOCATION_SERVICE = "http://localhost:7777";

    public BookingServiceImpl(PassengerRepository passengerRepository,
                              BookingRepository bookingRepository,
                              LocationServiceApi locationServiceApi ,
                              DriverRepository driverRepository,
                              PickMeSocketApi pickMeSocketApi) {
        this.passengerRepository = passengerRepository;
        this.bookingRepository = bookingRepository;
        this.restTemplate = new RestTemplate();
        this.locationServiceApi = locationServiceApi;
        this.driverRepository = driverRepository;
        this.pickMeSocketApi = pickMeSocketApi;
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
        Booking newBooking = bookingRepository.save(booking);

        NearbyDriversRequestDto request = NearbyDriversRequestDto.builder()
                .latitude(createBookingRequestDto.getStartLocation().getLatitude())
                .longitude(createBookingRequestDto.getStartLocation().getLongitude())
                .build();
        processNearByDriversAsync(request,createBookingRequestDto,newBooking.getId());
        return CreateBookingResponseDto.builder()
                .bookingId(newBooking.getId())
                .bookingStatus(newBooking.getBookingStatus().toString())
                .build();
    }

    @Override
    @KafkaListener(topics = "PickMeApp-SocketPublisher")
    public void updateBooking(UpdateBookingRequestDto updateBookingRequestDto) {
        Optional<Driver> driverOptional = driverRepository.findById(updateBookingRequestDto.getDriverId().get());
        if(driverOptional.isPresent()){
            bookingRepository.updateBookingStatusAndDriverById(updateBookingRequestDto.getBookingId(),BookingStatus.valueOf(updateBookingRequestDto.getStatus()),driverOptional.get());

        }
        Optional<Booking> booking = bookingRepository.findById(updateBookingRequestDto.getBookingId());
        UpdateBookingResponseDto updateBookingResponseDto = UpdateBookingResponseDto.builder()
                .bookingId(updateBookingRequestDto.getBookingId())
                .status(booking.get().getBookingStatus())
                .driver(Optional.ofNullable(booking.get().getDriver()))
                .build();
        System.out.println(updateBookingResponseDto.toString());
    }

    public void processNearByDriversAsync(NearbyDriversRequestDto nearbyDriversRequestDto,CreateBookingRequestDto createBookingRequestDto,Long bookingId){
        Call<DriverLocationDto[]> call = locationServiceApi.getNearsetDrivers(nearbyDriversRequestDto);
        call.enqueue(new Callback<DriverLocationDto[]>() {
            @Override
            public void onResponse(Call<DriverLocationDto[]> call, Response<DriverLocationDto[]> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<DriverLocationDto> driverLocations = Arrays.asList(response.body());
                    List<Long> driverIds = new ArrayList<>();
                    driverLocations.forEach(driverLocationDto -> {
                        System.out.println(driverLocationDto.getDriverId() + " " + "lat: " + driverLocationDto.getLatitude() + "long: " + driverLocationDto.getLongitude());
                        driverIds.add(Long.valueOf(driverLocationDto.getDriverId()));
                    });
                    raisedRideRequestAsync(RideRequestDto.builder()
                            .passengerId(createBookingRequestDto.getPassengerId())
                            .startLocation(createBookingRequestDto.getStartLocation())
                            .endLocation(createBookingRequestDto.getEndLocation())
                            .driverId(driverIds)
                            .bookingId(bookingId)
                            .build());
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

    private void raisedRideRequestAsync(RideRequestDto rideRequestDto){
        Call<Boolean> call = pickMeSocketApi.raiseRideRequest(rideRequestDto);
        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Boolean result = response.body();
                    System.out.println("Driver Response is "+result.toString());
                }else{
                    System.out.println("Request Failed "+response.message());
                }

            }
            @Override
            public void onFailure(Call<Boolean> call, Throwable throwable) {
              throwable.printStackTrace();
            }
        });

    }
}
