package app.fourthink.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

@Entity
@Table(name = "message")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "recipient_kind", nullable = false)
    @Enumerated(EnumType.STRING)
    private RecipientKind recipientKind;

    @Column(name = "recipient_id", nullable = false)
    private Long recipientId;

    @Column(nullable = false, length = 500)
    private String body;

    @Column(name = "read_flag", nullable = false)
    private boolean read;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false)
    private Date createdAt;

    public Message() {
    }

    public Message(RecipientKind recipientKind, Long recipientId, String body) {
        this.recipientKind = recipientKind;
        this.recipientId = recipientId;
        this.body = body;
        this.read = false;
        this.createdAt = new Date();
    }

    public Long getId() {
        return id;
    }

    public RecipientKind getRecipientKind() {
        return recipientKind;
    }

    public Long getRecipientId() {
        return recipientId;
    }

    public String getBody() {
        return body;
    }

    public boolean isRead() {
        return read;
    }

    public void markRead() {
        this.read = true;
    }

    public Date getCreatedAt() {
        return createdAt;
    }
}
