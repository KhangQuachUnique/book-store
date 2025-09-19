package model;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Category entity representing categories table in the database. Supports
 * hierarchical category
 * structure with parent-child relationships.
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
