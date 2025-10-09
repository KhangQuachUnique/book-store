package model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "\"categories\"")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "\"id\"")
    private long id;

    @Column(name = "\"name\"", nullable = false)
    private String name;

    @Column(name = "\"isLeaf\"")
    private boolean isLeaf;

    @Column(name = "\"createdAt\"")
    private Timestamp createdAt;

    // Relationships
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "\"parentId\"")
    private Category parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Category> children;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Book> books;

    @Transient
    private Long parentIdCache;

    public Long getParentId() {
        if (parent != null) {
            return parent.getId();
        }
        return parentIdCache;
    }

    public void setParentId(Long parentId) {
        this.parentIdCache = parentId;
        if (parentId == null) {
            this.parent = null;
            return;
        }
        if (this.parent == null) {
            this.parent = new Category();
        }
        this.parent.setId(parentId);
    }

    public void setParent(Category parent) {
        this.parent = parent;
        this.parentIdCache = parent != null ? parent.getId() : null;
    }

    public boolean getIsLeaf() {
        return isLeaf;
    }

    public void setIsLeaf(boolean leaf) {
        this.isLeaf = leaf;
    }
}