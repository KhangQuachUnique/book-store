package model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "\"wishLists\"")
public class WishList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    //Transient fields
    @Transient
    private int totalItems;

    @Transient
    private int totalPages;

    @Transient
    private int currentPage;

    @Transient
    private int pageSize;


    // Relationships
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "\"userId\"", nullable = false)
    private User user;

    @OneToMany(mappedBy = "wishList", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<WishListItem> items;


    public void setUpPagination(int currentPage, int pageSize) {
        this.currentPage = currentPage;
        this.pageSize = pageSize;
        if (items != null) {
            this.totalItems = items.size();
            this.totalPages = (int) Math.ceil((double) totalItems / pageSize);
        } else {
            this.totalItems = 0;
            this.totalPages = 0;
        }
    }
}