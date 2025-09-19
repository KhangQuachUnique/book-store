package model;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Address entity representing addresses table in the database. Contains user
 * delivery addresses
 * with default address support.
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
