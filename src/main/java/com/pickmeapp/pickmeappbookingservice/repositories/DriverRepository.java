package com.pickmeapp.pickmeappbookingservice.repositories;

import com.pickme.pickmeappentityservice.models.Driver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DriverRepository extends JpaRepository<Driver,Long> {
}
