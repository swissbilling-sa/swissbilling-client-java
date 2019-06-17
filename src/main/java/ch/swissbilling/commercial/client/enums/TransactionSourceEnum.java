package ch.swissbilling.commercial.client.enums;

public enum TransactionSourceEnum {
	UNDEFINED(0),
	COMMERCIAL(1),
	COMMERCIAL_PRINTER(2),
	COMMERCIAL_DRAG_DROP(4),
	COMMERCIAL_BATCH(8),
	COMMERCIAL_BROWSER(16),
	LS(32),
	LS_JOB(64),
	MINVOICE(128),
	LEGACY(256),
	COMMERCIAL_CLIENT_API(512);

	private final int value;

	TransactionSourceEnum(int id) {
		this.value = id;
	}

	public int getValue() {
		return value;
	}
}
