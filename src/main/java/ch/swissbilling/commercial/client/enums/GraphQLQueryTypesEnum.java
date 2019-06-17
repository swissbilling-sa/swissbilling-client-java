package ch.swissbilling.commercial.client.enums;

public enum GraphQLQueryTypesEnum {
	QUERY("query"),
	MUTATION("mutation");

	public String value;

	GraphQLQueryTypesEnum(String id) {
		this.value = id;
	}

	public String getValue() {
		return value;
	}		
}
