package ch.swissbilling.commercial.client.enums;

public enum ReservationStatusEnum {
    Open(0),
    USED(1),
    VALIDITY_ENDED(2),
    BLOCKED(3),
    DELETED(4),
    REFUSED(5),
    FAIL_TO_PRE_SCREENING(6);

    private final int value;

    ReservationStatusEnum(int id) {
        this.value = id;
    }

    public int getValue() {
        return value;
    }
}
