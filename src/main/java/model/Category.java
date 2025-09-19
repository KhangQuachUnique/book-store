package model;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Model class representing a category.
 */
public class Category implements Serializable {
    private long id;
    private String name;
    private Long parentId;
    private boolean isLeaf;
    private Timestamp createdAt;

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Long getParentId() { return parentId; }
    public void setParentId(Long parentId) { this.parentId = parentId; }
    public boolean getIsLeaf() { return isLeaf; }
    public void setIsLeaf(boolean isLeaf) { this.isLeaf = isLeaf; }
    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
}