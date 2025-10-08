package model;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

@Data
@Entity
@Table(name = "\"addresses\"")
public class Address implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "\"id\"")
    private long id;

    @Column(name = "\"userId\"", insertable = false, updatable = false)
    private long userId;

    @Column(name = "\"address\"", nullable = false)
    private String address;

    @Column(name = "\"isDefault\"")
    private boolean isDefaultAddress;

    @Column(name = "\"createdAt\"")
    private Timestamp createdAt;

    // Relationships
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "\"userId\"", nullable = false)
    private User user;

    @Override
    public String toString() {
        return this.address;
    }
}