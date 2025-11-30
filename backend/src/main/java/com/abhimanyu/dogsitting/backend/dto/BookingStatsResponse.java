package com.abhimanyu.dogsitting.backend.dto;

public class BookingStatsResponse {

    private long total;
    private long pending;
    private long confirmed;
    private long canceled;

    public BookingStatsResponse(long total, long pending, long confirmed, long canceled) {
        this.total = total;
        this.pending = pending;
        this.confirmed = confirmed;
        this.canceled = canceled;
    }

    public long getTotal() {
        return total;
    }

    public long getPending() {
        return pending;
    }

    public long getConfirmed() {
        return confirmed;
    }

    public long getCanceled() {
        return canceled;
    }
}
