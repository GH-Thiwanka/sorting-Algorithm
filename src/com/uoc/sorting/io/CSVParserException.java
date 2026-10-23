package com.uoc.sorting.io;

/**
 * Custom exception thrown when encountering CSV parsing or data format anomalies.
 * Part of Member 1's contribution: Robust File Upload and Dataset Handling.
 *
 * Course: IC4302 - Programming III
 * Department of ICT, Faculty of Technology, University of Colombo
 */
public class CSVParserException extends Exception {
    private final int lineNumber;

    public CSVParserException(String message) {
        super(message);
        this.lineNumber = -1;
    }

    public CSVParserException(String message, int lineNumber) {
        super(String.format("Error at line %d: %s", lineNumber, message));
        this.lineNumber = lineNumber;
    }

    public CSVParserException(String message, Throwable cause) {
        super(message, cause);
        this.lineNumber = -1;
    }

    public int getLineNumber() {
        return lineNumber;
    }
}
