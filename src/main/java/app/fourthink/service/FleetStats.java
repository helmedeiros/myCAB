package app.fourthink.service;

public final class FleetStats {

    private final int total;
    private final int free;
    private final int busy;
    private final int offline;

    public FleetStats(int total, int free, int busy, int offline) {
        this.total = total;
        this.free = free;
        this.busy = busy;
        this.offline = offline;
    }

    public int getTotal() {
        return total;
    }

    public int getFree() {
        return free;
    }

    public int getBusy() {
        return busy;
    }

    public int getOffline() {
        return offline;
    }
}
