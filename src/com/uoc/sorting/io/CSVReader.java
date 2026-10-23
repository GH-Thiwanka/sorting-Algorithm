package com.uoc.sorting.io;

import com.uoc.sorting.model.Dataset;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles uploading, reading, and parsing comma-separated value (.csv) datasets.
 * Implements a robust RFC 4180-compliant state parser supporting quoted fields,
 * embedded commas, escaped quotes, and graceful whitespace handling.
 * 
 * Part of Member 1's contribution: File upload functionality and dataset handling.
 *
 * Course: IC4302 - Programming III
 * Department of ICT, Faculty of Technology, University of Colombo
 */
public class CSVReader {

    /**
     * Reads a CSV file from the filesystem and transforms it into a structured Dataset model.
     *
     * @param file the CSV file selected by the user
     * @return Dataset populated with headers, rows, and analyzed column metadata
     * @throws IOException if an I/O error occurs
     * @throws CSVParserException if the file is invalid, empty, or unparseable
     */
    public static Dataset read(File file) throws IOException, CSVParserException {
        if (file == null) {
            throw new CSVParserException("No file provided for upload.");
        }
        if (!file.exists()) {
            throw new CSVParserException("Selected file does not exist: " + file.getAbsolutePath());
        }
        if (!file.isFile()) {
            throw new CSVParserException("Selected path is a directory, not a file: " + file.getAbsolutePath());
        }
        if (file.length() == 0) {
            throw new CSVParserException("Uploaded CSV file is completely empty (0 bytes).");
        }

        List<String> headers = null;
        List<String[]> rows = new ArrayList<>();
        int lineNumber = 0;

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {
            
            String line;
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                // Skip empty or purely whitespace lines
                if (line.trim().isEmpty()) {
                    continue;
                }

                List<String> tokens = parseLine(line);

                if (headers == null) {
                    if (tokens.isEmpty()) {
                        throw new CSVParserException("Header line is empty.", lineNumber);
                    }
                    // Clean potential BOM character on first header column
                    if (!tokens.isEmpty() && tokens.get(0).startsWith("\uFEFF")) {
                        tokens.set(0, tokens.get(0).substring(1));
                    }
                    headers = tokens;
                } else {
                    // Normalize row length to match column header count
                    String[] rowArray = new String[headers.size()];
                    for (int i = 0; i < headers.size(); i++) {
                        if (i < tokens.size()) {
                            rowArray[i] = tokens.get(i).trim();
                        } else {
                            rowArray[i] = ""; // Pad missing columns with empty string
                        }
                    }
                    rows.add(rowArray);
                }
            }
        }

        if (headers == null || headers.isEmpty()) {
            throw new CSVParserException("CSV file contains no valid headers.");
        }

        if (rows.isEmpty()) {
            throw new CSVParserException("CSV file contains header row but no data rows.");
        }

        return new Dataset(file.getName(), headers, rows);
    }

    /**
     * Parses a single CSV line into distinct field tokens according to RFC 4180 rules.
     * Handles quoted fields, double-quote escapes, and commas inside quotes.
     *
     * @param line Raw string line from the CSV file
     * @return List of parsed field strings
     */
    public static List<String> parseLine(String line) {
        List<String> tokens = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        boolean inQuotes = false;
        int len = line.length();

        for (int i = 0; i < len; i++) {
            char c = line.charAt(i);

            if (c == '\"') {
                if (inQuotes && i + 1 < len && line.charAt(i + 1) == '\"') {
                    // Escaped quote: "" -> "
                    sb.append('\"');
                    i++; // skip next quote
                } else {
                    // Toggle quote state
                    inQuotes = !inQuotes;
                }
            } else if (c == ',' && !inQuotes) {
                tokens.add(sb.toString().trim());
                sb.setLength(0);
            } else {
                sb.append(c);
            }
        }

        tokens.add(sb.toString().trim());
        return tokens;
    }
}
