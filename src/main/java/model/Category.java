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

    // For backward compatibility with JSPs expecting parentId
    public Long getParentId() {
        return (parent != null) ? parent.getId() : null;
    }

    // For backward compatibility with JSPs using setParentId
    public void setParentId(Long parentId) {
        // This is just a placeholder - actual parent setting is done in service layer
        // with proper entity lookup
    }
}