package model;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * Generic API response wrapper for REST endpoints.
 * Provides consistent response structure across all API calls.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponse {
    
    private boolean success;
    private String message;
    private Object data;
}
