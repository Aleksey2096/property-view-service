package com.example.propertyview.repository;

import com.example.propertyview.model.entity.Amenity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AmenityRepository extends JpaRepository<Amenity, Long> {

    Optional<Amenity> findByTitle(String title);

    @Query("SELECT a.title AS key, COUNT(h) AS value FROM Amenity a LEFT JOIN a.hotels h GROUP BY a.title")
    List<Object[]> countHotelsByAmenity();
}
