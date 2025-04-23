package com.example.swift_api;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class SwiftCodeService {
    private final SwiftCodeRepository repository;

    public SwiftCodeService(SwiftCodeRepository repository) {
        this.repository = repository;
    }

    public Optional<SwiftCode> findByCode(String code) {
        return repository.findBySwiftCode(code);
    }

    public List<SwiftCode> findAll(String country) {
        if (repository.findAllByCountryISO2(country) != null) {
            return repository.findAllByCountryISO2(country.toUpperCase());
        } else {
            return null;
        }
    }

    public boolean exists(String code) {
        return repository.existsBySwiftCode(code);
    }

    public SwiftCode save(SwiftCode swiftCode) {
        swiftCode.setCountryISO2(swiftCode.getCountryISO2().toUpperCase());
        swiftCode.setCountryName(swiftCode.getCountryName().toUpperCase());
        return repository.save(swiftCode);
    }
    @Transactional
    public void deleteBySwiftCode(String swiftCode) {
        repository.deleteBySwiftCode(swiftCode);
    }

    public List<SwiftCode> getAll() {
        return repository.findAll();
    }

}
