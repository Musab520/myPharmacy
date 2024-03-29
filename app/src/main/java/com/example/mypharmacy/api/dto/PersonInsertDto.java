package com.example.mypharmacy.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PersonInsertDto {
    private String firstName;
    private String lastName;
    private String gender;
    private String address;
    private float weight;
    private float height;
    private int phoneNumber;
    private LocalDate birthDate;
    private String maritalStatus;
    private String bloodType;
}
