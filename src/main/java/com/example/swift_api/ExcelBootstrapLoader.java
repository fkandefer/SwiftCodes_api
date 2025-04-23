package com.example.swift_api;

import org.apache.poi.ss.usermodel.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.InputStream;

@Component
public class ExcelBootstrapLoader implements CommandLineRunner {

    private final SwiftCodeRepository repository;

    public ExcelBootstrapLoader(SwiftCodeRepository repository) {
        this.repository = repository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (repository.count() > 0) {
            return; // nie ładuj jeśli dane już istnieją
        }

        InputStream inputStream = new ClassPathResource("swift-codes.xlsx").getInputStream();
        Workbook workbook = WorkbookFactory.create(inputStream);
        Sheet sheet = workbook.getSheetAt(0);

        for (Row row : sheet) {
            if (row.getRowNum() == 0) continue; // pomiń nagłówki

            SwiftCode code = new SwiftCode();
            code.setCountryISO2(getString(row, 0));
            code.setSwiftCode(getString(row, 1));
            code.setCodeType(getString(row, 2));
            code.setName(getString(row, 3));
            code.setAddress(getString(row, 4));
            code.setTownName(getString(row, 5));
            code.setCountryName(getString(row, 6));
            code.setTimeZone(getString(row, 7));


            repository.save(code);
        }

        workbook.close();
        inputStream.close();
    }

    private String getString(Row row, int colIndex) {
        Cell cell = row.getCell(colIndex);
        return (cell != null) ? cell.toString().trim() : null;
    }

}
