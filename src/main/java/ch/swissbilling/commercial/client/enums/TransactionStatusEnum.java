package ch.swissbilling.commercial.client.enums;

public enum TransactionStatusEnum {
	OPEN(0),
	IN_PROCESS(1),
	CANCELED(2),
	SENT(3),
	REJECTED(4),
	COMMERCIAL_ERROR(5),
	SR_ERROR(6),
	ACKNOWLEDGED(7),
	REQUIRE_VALIDATION(8),
	REJECTED_BY_PRESCREENING(9),
	FAIL_TO_PRESCREENING(10),
	GUARANTEE_TYPE_BEHAVIOR_ERROR(11);

	private final int value;

	TransactionStatusEnum(int id) {
		this.value = id;
	}

	public int getValue() {
		return value;
	}
}
