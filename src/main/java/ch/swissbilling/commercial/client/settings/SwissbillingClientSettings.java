package ch.swissbilling.commercial.client.settings;

import ch.swissbilling.commercial.client.enums.SwissbillingEnvironment;

public class SwissbillingClientSettings {
	public String merchantEmail;
	public String merchantPassword;
	public SwissbillingEnvironment environment;
	
	public SwissbillingClientSettings(String email, String password, SwissbillingEnvironment env){
		this.merchantEmail = email;
		this.merchantPassword = password;
		this.environment = env;
	}
}
