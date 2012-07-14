package app.fourthink.model;

public final class RecipientAddress {

    private final RecipientKind kind;
    private final Long id;

    public RecipientAddress(RecipientKind kind, Long id) {
        if (kind == null) throw new IllegalArgumentException("kind required");
        if (id == null) throw new IllegalArgumentException("id required");
        this.kind = kind;
        this.id = id;
    }

    public RecipientKind getKind() {
        return kind;
    }

    public Long getId() {
        return id;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (!(other instanceof RecipientAddress)) return false;
        RecipientAddress o = (RecipientAddress) other;
        return kind == o.kind && id.equals(o.id);
    }

    @Override
    public int hashCode() {
        return kind.hashCode() * 31 + id.hashCode();
    }

    @Override
    public String toString() {
        return kind + "/" + id;
    }
}
