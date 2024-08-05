package com.nishantgupta.JMA2.Validators;

public class NumericValidator implements Validator {
    private static final NumericValidator instance = new NumericValidator();

    private NumericValidator() {
        // private constructor to prevent instantiation
    }

    public static NumericValidator getInstance() {
        return instance;
    }

    @Override
    public boolean validate(String input) {
        try {
            Integer.parseInt(input);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}


