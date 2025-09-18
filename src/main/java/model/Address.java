package model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.sql.Timestamp;

/**
 * Address entity representing addresses table in the database.
 * Contains user delivery addresses with default address support.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Address {
    
    private Long id;
    private Long userId;
    private String address;
    private Boolean isDefaultAddress;
    
    // Audit fields
    private Timestamp createdAt;
}
