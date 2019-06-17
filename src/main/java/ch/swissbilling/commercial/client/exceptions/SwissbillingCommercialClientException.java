package ch.swissbilling.commercial.client.exceptions;

public class SwissbillingCommercialClientException extends Exception{
	public static final String LOGIN_FAILED = "Failed to init SwissbillingCommercialClient, "
			+ "please make sure that your username and password are correct "
			+ "and if you are using the correct environment endpoints.";
	
	public static final String NO_SHOP = "Failed to init SwissbillingCommercialClient, "
			+"the current merchant has no shop.";
	
	public static final String FAIL_TO_CONFIGURE_ENDPOINTS = "Fail to configure environment endpoints.";

	public static final String CURRENCY_NOT_SUPPORTED = "The %s currency is not supported.";
	
	public SwissbillingCommercialClientException(String errorMessage) {
		super(errorMessage);
	}
}
