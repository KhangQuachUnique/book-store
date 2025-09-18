package model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.sql.Timestamp;

/**
 * Category entity representing categories table in the database.
 * Supports hierarchical category structure with parent-child relationships.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Category {
    
    private Long id;
    private String name;
    
    // Hierarchical structure
    private Long parentId;
    private Boolean isLeaf;
    
    // Audit fields
    private Timestamp createdAt;
}
