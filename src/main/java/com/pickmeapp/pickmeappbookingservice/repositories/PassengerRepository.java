package com.pickmeapp.pickmeappbookingservice.repositories;


import com.pickme.pickmeappentityservice.models.Passenger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.ResponseBody;

@Repository
public interface PassengerRepository extends JpaRepository<Passenger,Long> {
}
