package model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.OffsetDateTime;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "promotions")
public class Promotion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "\"code\"", unique = true, nullable = false)
    private String code;

    @Column(name = "\"discount\"", nullable = false)
    private double discount;

    @Column(name = "\"expireAt\"", columnDefinition = "TIMESTAMP WITH TIME ZONE", nullable = false)
    private OffsetDateTime expireAt;

    @OneToMany(mappedBy = "promotion", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<Order> orders;

    public boolean isValid() {
        return expireAt != null && expireAt.isAfter(OffsetDateTime.now());
    }

    // ✅ Getter mới: giúp JSP hiểu được kiểu Date khi format
    public Date getExpireAtDate() {
        return expireAt == null ? null : Date.from(expireAt.toInstant());
    }
}