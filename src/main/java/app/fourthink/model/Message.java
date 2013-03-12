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

    @Column(name = "source_customer_id")
    private Long sourceCustomerId;

    @Column(name = "pickup_address", length = 250)
    private String pickupAddress;

    @Column(name = "destination_address", length = 250)
    private String destinationAddress;

    public Message() {
    }

    public Message(RecipientKind recipientKind, Long recipientId, String body) {
        this.recipientKind = recipientKind;
        this.recipientId = recipientId;
        this.body = body;
        this.read = false;
        this.createdAt = new Date();
    }

    public Message(RecipientKind recipientKind, Long recipientId, String body,
                    Long sourceCustomerId) {
        this(recipientKind, recipientId, body);
        this.sourceCustomerId = sourceCustomerId;
    }

    public Message(RecipientKind recipientKind, Long recipientId, String body,
                    Long sourceCustomerId,
                    String pickupAddress, String destinationAddress) {
        this(recipientKind, recipientId, body, sourceCustomerId);
        this.pickupAddress = pickupAddress;
        this.destinationAddress = destinationAddress;
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

    public Long getSourceCustomerId() {
        return sourceCustomerId;
    }

    public String getPickupAddress() {
        return pickupAddress;
    }

    public String getDestinationAddress() {
        return destinationAddress;
    }
}
