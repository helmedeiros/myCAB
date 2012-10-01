package app.fourthink.model;

public final class MessagePayload {

    private final Long id;
    private final String body;
    private final long createdAt;

    public MessagePayload(Long id, String body, long createdAt) {
        this.id = id;
        this.body = body;
        this.createdAt = createdAt;
    }

    public Long getId() { return id; }
    public String getBody() { return body; }
    public long getCreatedAt() { return createdAt; }

    public static MessagePayload of(Message message) {
        return new MessagePayload(message.getId(), message.getBody(),
                message.getCreatedAt().getTime());
    }
}
