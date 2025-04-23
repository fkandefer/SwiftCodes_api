package com.example.swift_api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class SwiftCodeControllerTest {

    private MockMvc mockMvc;

    @Mock
    private SwiftCodeService swiftCodeService;

    @InjectMocks
    private SwiftCodeController swiftCodeController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(swiftCodeController).build();
    }

    @Test
    void testGetBySwiftCode() throws Exception {
        SwiftCode swiftCode = new SwiftCode();
        swiftCode.setSwiftCode("12345678XXX");
        swiftCode.setName("Example Bank");
        swiftCode.setAddress("456 Example Ave");
        swiftCode.setCountryISO2("GB");
        swiftCode.setCountryName("UNITED KINGDOM");

        when(swiftCodeService.findByCode("12345678XXX")).thenReturn(Optional.of(swiftCode));

        mockMvc.perform(get("/v1/swift-codes/12345678XXX"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.swiftCode").value("12345678XXX"))
                .andExpect(jsonPath("$.isHeadquarter").value(true));
    }

    @Test
    void testGetByCountry() throws Exception {
        SwiftCode swiftCode1 = new SwiftCode();
        swiftCode1.setSwiftCode("XYZ12345XXX");
        swiftCode1.setName("Example Bank");
        swiftCode1.setAddress("789 Example Blvd");
        swiftCode1.setCountryISO2("CA");
        swiftCode1.setCountryName("CANADA");

        SwiftCode swiftCode2 = new SwiftCode();
        swiftCode2.setSwiftCode("XYZ12345YYY");
        swiftCode2.setName("Example Bank Branch");
        swiftCode2.setAddress("101 Example St");
        swiftCode2.setCountryISO2("CA");
        swiftCode2.setCountryName("CANADA");

        when(swiftCodeService.findAll("CA")).thenReturn(List.of(swiftCode1, swiftCode2));

        mockMvc.perform(get("/v1/swift-codes/country/CA"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.countryISO2").value("CA"))
                .andExpect(jsonPath("$.swiftCodes").isArray())
                .andExpect(jsonPath("$.swiftCodes[0].swiftCode").value("XYZ12345XXX"));
    }

    @Test
    void testAddSwiftCode() throws Exception {
        SwiftCode newSwiftCode = new SwiftCode();
        newSwiftCode.setSwiftCode("12345678XXX");
        newSwiftCode.setName("New Bank");
        newSwiftCode.setAddress("New St");
        newSwiftCode.setCountryISO2("PL");
        newSwiftCode.setCountryName("POLAND");

        when(swiftCodeService.exists("12345678XXX")).thenReturn(false);
        when(swiftCodeService.save(any(SwiftCode.class))).thenReturn(newSwiftCode);

        mockMvc.perform(post("/v1/swift-codes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newSwiftCode)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Swift code saved: 12345678XXX"));
    }

    @Test
    void testDeleteSwiftCode() throws Exception {
        when(swiftCodeService.exists("12345678XXX")).thenReturn(true);

        mockMvc.perform(delete("/v1/swift-codes/12345678XXX"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Swift code deleted: 12345678XXX"));
    }
}