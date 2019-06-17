package ch.swissbilling.commercial.client.enums;

public enum DebtorTypeEnum {
	INDIVIDUAL(0),
	BUSINESS(1);

	private final int value;

	DebtorTypeEnum(int id) {
		this.value = id;
	}

	public int getValue() {
		return value;
	}
}