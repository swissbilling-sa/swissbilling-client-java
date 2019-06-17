package ch.swissbilling.commercial.client.enums;

public enum SwissbillingEnvironment {
    PRODUCTION("production"),
    PRE_PRODUCTION("preProduction"),
    STAGING("staging"),
    DEV("dev"),
    LOCALHOST("localhost");

    private String value;

    SwissbillingEnvironment(String id) {
        this.value = id;
    }

    public String getValue() {
        return value;
    }
}