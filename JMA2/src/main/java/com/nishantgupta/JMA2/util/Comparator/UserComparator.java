package com.nishantgupta.JMA2.util.Comparator;



public class UserComparator {
 
    public static int countCharactersWithoutSpaces(String input) {
        // Use replaceAll to remove spaces, then get the length
    	
    	
        return input.replaceAll("\\s", "").length();
    }

 
    }


