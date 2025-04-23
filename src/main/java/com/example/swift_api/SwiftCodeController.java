package com.example.swift_api;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1/swift-codes")
public class SwiftCodeController {

    private final SwiftCodeService service;

    public SwiftCodeController(SwiftCodeService service) {
        this.service = service;
    }

    // Endpoint 1: Get SwiftCode by code
    @GetMapping("/{swiftCode}")
    public ResponseEntity<?> getBySwiftCode(@PathVariable String swiftCode) {
        SwiftCode code = service.findByCode(swiftCode)
                .orElseThrow(() -> new SwiftCodeNotFoundException("Swift code not found"));

        if (code.isHeadquarter()) {
            List<SwiftCode> branches = service.getAll().stream()
                    .filter(c -> !c.getSwiftCode().equals(swiftCode)
                            && c.getSwiftCode().startsWith(swiftCode.substring(0, 8)))
                    .toList();

            Map<String, Object> response = new HashMap<>();
            response.put("address", code.getAddress());
            response.put("bankName", code.getName());
            response.put("countryISO2", code.getCountryISO2());
            response.put("countryName", code.getCountryName());
            response.put("isHeadquarter", true);
            response.put("swiftCode", code.getSwiftCode());
            response.put("branches", branches.stream().map(branch -> Map.of(
                    "address", branch.getAddress(),
                    "bankName", branch.getName(),
                    "countryISO2", branch.getCountryISO2(),
                    "isHeadquarter", branch.isHeadquarter(),
                    "swiftCode", branch.getSwiftCode()
            )).toList());

            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.ok(Map.of(
                    "address", code.getAddress(),
                    "bankName", code.getName(),
                    "countryISO2", code.getCountryISO2(),
                    "isHeadquarter", false,
                    "swiftCode", code.getSwiftCode()
            ));
        }
    }

    // Endpoint 2: Get SwiftCodes by country
    @GetMapping("/country/{countryISO2}")
    public ResponseEntity<?> getByCountry(@PathVariable String countryISO2) {
        List<SwiftCode> codes = service.findAll(countryISO2);
        if (codes.isEmpty()) {
            throw new SwiftCodeNotFoundException("No data for country " + countryISO2);
        }

        return ResponseEntity.ok(Map.of(
                "countryISO2", countryISO2.toUpperCase(),
                "countryName", codes.get(0).getCountryName(),
                "swiftCodes", codes.stream().map(c -> Map.of(
                        "address", c.getAddress(),
                        "bankName", c.getName(),
                        "countryISO2", c.getCountryISO2(),
                        "isHeadquarter", c.isHeadquarter(),
                        "swiftCode", c.getSwiftCode()
                )).toList()
        ));
    }

    // Endpoint 3: Add a new SwiftCode
    @PostMapping
    public ResponseEntity<?> addSwiftCode(@Valid @RequestBody SwiftCode swiftCode) {
        if (service.exists(swiftCode.getSwiftCode())) {
            return ResponseEntity.status(400).body(Map.of("message", "Swift code already exists"));
        }
        SwiftCode saved = service.save(swiftCode);
        return ResponseEntity.ok(Map.of("message", "Swift code saved: " + saved.getSwiftCode()));
    }

    // Endpoint 4: Delete a SwiftCode
    @DeleteMapping("/{swiftCode}")
    public ResponseEntity<?> deleteSwiftCode(@PathVariable String swiftCode) {
        if (!service.exists(swiftCode)) {
            throw new SwiftCodeNotFoundException("Swift code not found");
        }

        service.deleteBySwiftCode(swiftCode);
        return ResponseEntity.ok(Map.of("message", "Swift code deleted: " + swiftCode));
    }
}