package com.example.swift_api;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Entity
@Data
public class SwiftCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "Country ISO2 code is required")
    private String countryISO2;

    @NotBlank(message = "Swift code is required")
    private String swiftCode;

    private String codeType;

    @NotBlank(message = "Bank name is required")
    private String name;

    private String address;

    private String townName;

    @NotBlank(message = "Country name is required")
    private String countryName;

    private String timeZone;

    @Transient
    public boolean isHeadquarter() {
        return swiftCode != null && swiftCode.endsWith("XXX");
    }
}