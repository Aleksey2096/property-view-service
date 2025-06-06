package com.example.propertyview.repository;

import com.example.propertyview.model.entity.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface HotelRepository extends JpaRepository<Hotel, Long> {

    @Query("SELECT h FROM Hotel h WHERE " +
            "(:name IS NULL OR LOWER(h.name) LIKE LOWER(CONCAT('%', :name, '%'))) AND " +
            "(:brand IS NULL OR LOWER(h.brand) LIKE LOWER(CONCAT('%', :brand, '%'))) AND " +
            "(:city IS NULL OR LOWER(h.address.city) = LOWER(:city)) AND " +
            "(:country IS NULL OR LOWER(h.address.country) = LOWER(:country))")
    List<Hotel> searchByParams(@Param("name") String name,
                               @Param("brand") String brand,
                               @Param("city") String city,
                               @Param("country") String country);

    @Query("SELECT h.address.city AS key, COUNT(h) AS value FROM Hotel h GROUP BY h.address.city")
    List<Object[]> countHotelsByCity();

    @Query("SELECT h.address.country AS key, COUNT(h) AS value FROM Hotel h GROUP BY h.address.country")
    List<Object[]> countHotelsByCountry();

    @Query("SELECT h.brand AS key, COUNT(h) AS value FROM Hotel h GROUP BY h.brand")
    List<Object[]> countHotelsByBrand();
}
