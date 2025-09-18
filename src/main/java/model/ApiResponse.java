package model;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponse {
    private boolean success;
    private String message;
    private Object data;
}
