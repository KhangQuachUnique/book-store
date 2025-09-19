package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Status entity representing status table in the database. Contains order
 * status definitions (e.g.,
 * pending, processing, shipped, delivered).
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Status {

    private Long id;
    private String name;
}
