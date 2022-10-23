package com.company.exceptions;

public class DateFormatException extends  Exception{

    public DateFormatException() {
        this("Wrong Date Format");
    }

    public DateFormatException(String message) {
        super(message);
    }
}
