package model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "\"notifications\"")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "\"id\"")
    private Long id;

    @Column(name = "\"title\"", nullable = false)
    private String title;

    @Column(name = "\"message\"")
    private String message;

    @Column(name = "\"type\"")
    private String type;

    @Column(name = "\"createdAt\"")
    private Timestamp createdAt;

    @Column(name = "\"isRead\"")
    private Boolean isRead;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "\"userId\"")
    private User user;

    public Long getUserId() {
        return user != null ? user.getId() : null;
    }

    public void setUserId(Long userId) {
        if (userId == null) {
            this.user = null;
            return;
        }
        if (this.user == null) {
            this.user = new User();
        }
        this.user.setId(userId);
    }

    public void setRead(boolean read) {
        this.isRead = read;
    }

    public boolean getRead() {
        return Boolean.TRUE.equals(isRead);
    }
}