package ch.swissbilling.commercial.client.enums;

public enum GuaranteeTypeEnum {
	NONE(0),
	FULL_AMOUNT(1);

	public int value;

	GuaranteeTypeEnum(int id) {
		this.value = id;
	}

	public int getValue() {
		return value;
	}		
}
