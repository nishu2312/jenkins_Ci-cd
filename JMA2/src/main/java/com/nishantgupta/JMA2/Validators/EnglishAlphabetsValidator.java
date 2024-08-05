package com.nishantgupta.JMA2.Validators;

public class EnglishAlphabetsValidator implements Validator {
    private static final EnglishAlphabetsValidator instance = new EnglishAlphabetsValidator();

    private EnglishAlphabetsValidator() {
        // private constructor to prevent instantiation
    }

    public static EnglishAlphabetsValidator getInstance() {
        return instance;
    }

    @Override
    public boolean validate(String input) {
        return input.matches("^[a-zA-Z]*$");
    }
}

