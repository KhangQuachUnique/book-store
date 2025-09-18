package model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.io.Serializable;
import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WishListItem implements Serializable {
    private Long id;
    private Long userId;
    private Long bookId;
    private Timestamp createdAt;
    // Transient field for display purposes
    private Book book;
}