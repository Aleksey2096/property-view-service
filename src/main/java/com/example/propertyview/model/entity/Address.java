package com.example.propertyview.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "addresses")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "house_number", nullable = false)
    private Integer houseNumber;
    @Column(name = "street", nullable = false)
    private String street;
    @Column(name = "city", length = 120, nullable = false)
    private String city;
    @Column(name = "country", length = 120, nullable = false)
    private String country;
    @Column(name = "post_code", length = 50, nullable = false)
    private String postCode;
}
