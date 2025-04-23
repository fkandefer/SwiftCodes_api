package com.example.swift_api;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SwiftCodeRepository extends JpaRepository<SwiftCode, Integer> {
    Optional<SwiftCode> findBySwiftCode(String swiftCode);
    List<SwiftCode> findAllByCountryISO2(String countryCode);
    boolean existsBySwiftCode(String swiftCode);
    void deleteBySwiftCode(String swiftCode);
}
