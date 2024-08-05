package com.nishantgupta.JMA2.Validators;

public class ValidatorFactory {
    public static Validator createValidator(String parameterType) {
        if ("Numeric".equalsIgnoreCase(parameterType)) {
            return NumericValidator.getInstance();
        } else if ("Character".equalsIgnoreCase(parameterType)) {
            return EnglishAlphabetsValidator.getInstance();
        } else {
            throw new IllegalArgumentException("Invalid parameter type");
        }
    }
}


