package com.nishantgupta.JMA2.Entity;

import java.util.HashMap;
import java.util.Map;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PageInfo {
    private int total;
    private int offset;
    private int limit;

    // Constructors, getters, and setters
    
    public boolean hasNextPage() {
        return total > (offset + limit);
    }

    public boolean hasPreviousPage() {
        return offset > 0;
    }

    // Additional method to create a Map for the desired format
    public Map<String, Object> toMap() {
        Map<String, Object> pageInfoMap = new HashMap<>();
        pageInfoMap.put("total", total);
        pageInfoMap.put("hasNextPage", hasNextPage());
        pageInfoMap.put("hasPreviousPage", hasPreviousPage());
        return pageInfoMap;
    }
}

