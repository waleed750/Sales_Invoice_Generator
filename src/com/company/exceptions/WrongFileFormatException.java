package com.company.exceptions;

public class WrongFileFormatException extends Exception{

    public WrongFileFormatException(){
        this("Wrong File Format");
    }
    public WrongFileFormatException(String message) {
        super(message);
    }
}
