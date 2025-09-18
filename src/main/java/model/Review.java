package model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Review {
    private Long id;
    private Long bookId;
    private Long userId;
    private Integer rating; // 1-5
    private String comment;
    private Timestamp createdAt;
}