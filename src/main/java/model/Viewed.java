package model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Viewed {
    private Long id;
    private Long userId;
    private Long bookId;
    private Timestamp viewedAt;
}