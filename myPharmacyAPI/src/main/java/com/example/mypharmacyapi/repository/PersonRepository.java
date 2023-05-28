package com.example.mypharmacyapi.repository;

import com.example.mypharmacyapi.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

@Component
public interface PersonRepository extends JpaRepository<Person, Long> {

}
